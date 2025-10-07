# SkillLens GitHub Action — TypeScript Action Specification (MVP)

**Component:** Custom TypeScript Action (published on GitHub Marketplace)  
**Template:** [`actions/typescript-action`](https://github.com/actions/typescript-action)  
**Docs:** *Creating a JavaScript/TypeScript Action* (GitHub Docs) :contentReference[oaicite:0]{index=0}  
**Last Updated:** 2025‑10‑07

---

## 1) Purpose

A zero‑config Action that reads **existing PR review feedback** (human/AI), sends it to the SkillLens Proxy (your backend), then **creates/updates one PR comment** with **Hyperskill** learning recommendations. The Action is implemented as a **custom TypeScript Action** using the official template and is distributed on the Marketplace. :contentReference[oaicite:1]{index=1}

---

## 2) High‑Level Flow

1. Trigger on review activity (`pull_request_review`, `pull_request_review_comment`, `issue_comment` on PRs).
2. Use Octokit (via `@actions/github`) with the repo’s `GITHUB_TOKEN` to fetch:
   - **Inline** review comments for the PR.
   - **PR reviews** (Approve/Changes Requested/Comment body).
   - **Conversation** comments on the PR (via Issues API). :contentReference[oaicite:2]{index=2}
3. Normalize review text; trim long code fences (**no repository code** is read).
4. Request a GitHub **OIDC ID token** (job must grant `id-token: write`) and call the SkillLens Proxy (`POST /v1/recommendations`). :contentReference[oaicite:3]{index=3}
5. Receive `commentMarkdown` + `topics[]`; upsert a **single** PR comment (Issues API). :contentReference[oaicite:4]{index=4}

---

## 3) Repository & Build (TypeScript Action)

We scaffold from **`actions/typescript-action`** so we get a ready setup (TypeScript build, tests, dist checking, release guidance). The template builds a single **bundled** JS file in `dist/` (using Rollup) which must be **committed** so consumers don’t install dependencies at runtime. :contentReference[oaicite:5]{index=5}

**Initial steps**
- Click **Use this template** in `actions/typescript-action`, create your repo, clone, `npm install`, then **bundle**. The template’s README shows `npm run bundle` / `npm run all` (build + test) and explains why the bundled `dist/index.js` must be checked in. :contentReference[oaicite:6]{index=6}

**Key files**
```

action.yml              # Action metadata (name, inputs, outputs, runtime)
src/main.ts             # Entry point (Action logic)
**tests**/*.test.ts     # Jest tests
package.json            # scripts: build, test, lint, bundle
tsconfig*.json          # TypeScript config
rollup.config.ts        # Bundling for dist/index.js (from template)
dist/index.js           # Generated bundle (committed)

````

> GitHub’s docs also describe bundling with Rollup or `@vercel/ncc`; the template already includes Rollup configuration and scripts. :contentReference[oaicite:7]{index=7}

**Runtime**
- Node 20 (as per template `.node-version` and recommended in docs). :contentReference[oaicite:8]{index=8}

---

## 4) Action Metadata (`action.yml`)

### 4.1 Name & Description
```yaml
name: "SkillLens — PR Review → Hyperskill"
description: "Parses PR review feedback and posts learning recommendations from Hyperskill."
author: "Hyperskill / SkillLens"
runs:
  using: "node20"
  main: "dist/index.js"
branding:
  icon: "book-open"
  color: "purple"
````

### 4.2 Inputs

```yaml
inputs:
  skilllens-api-url:
    description: "SkillLens Proxy endpoint (e.g., https://<your-backend>/v1/recommendations)"
    required: true
  oidc-audience:
    description: "OIDC audience to request for ID token (matches backend expected 'aud')"
    required: false
    default: "skilllens.dev"
  default-language:
    description: "Fallback programming language for topics (e.g., Python)"
    required: false
    default: "Python"
  max-topics:
    description: "Maximum number of topics to include"
    required: false
    default: "5"
  min-confidence:
    description: "Minimum confidence threshold (0..1) for topics"
    required: false
    default: "0.65"
  comment-marker:
    description: "Hidden marker used to upsert a single PR comment"
    required: false
    default: "<!-- SkillLens:v0 -->"
  fail-on-proxy-error:
    description: "Fail the workflow if the proxy returns an error (true/false)"
    required: false
    default: "false"
```

> Note: We don’t require users to pass any LLM keys. The Action uses **GITHUB_TOKEN** to read comments and **OIDC** to authenticate to your backend. GitHub recommends using `GITHUB_TOKEN` for REST API access from Actions. ([GitHub Docs][1])

### 4.3 Outputs (Optional)

```yaml
outputs:
  topics-json:
    description: "JSON string of returned topics (for debugging/CI artifacts)"
  comment-url:
    description: "URL of the created/updated PR comment"
```

---

## 5) Usage (for consumers)

### 5.1 Recommended workflow (in a user’s repo)

```yaml
name: SkillLens (reviews → Hyperskill)

on:
  pull_request_review:
    types: [submitted, edited, dismissed]
  pull_request_review_comment:
    types: [created, edited]
  issue_comment:
    types: [created, edited]

permissions:
  contents: read
  pull-requests: write
  issues: write
  id-token: write

jobs:
  skilllens:
    if: ${{ github.event_name != 'issue_comment' || github.event.issue.pull_request }}
    runs-on: ubuntu-latest
    steps:
      - uses: <you>/skilllens-action@v1
        with:
          skilllens-api-url: https://<your-backend>/v1/recommendations
          oidc-audience: skilllens.dev
          default-language: Python
          max-topics: 5
          min-confidence: 0.65
```

* The triggers capture **review bodies**, **inline review comments**, and **PR conversation** comments. We guard `issue_comment` so it runs only on PRs. ([GitHub Docs][2])
* The job grants the required **permissions**: API reads, comment writes, and `id-token: write` for OIDC token issuance. ([GitHub Docs][3])

---

## 6) Internal Design (Action Logic)

### 6.1 Dependencies

* `@actions/core` (inputs/outputs, logging), `@actions/github` (Octokit), `undici` (if needed; Node 20 built‑in `fetch` works), `zod` (or `ajv`) for payload validation.

### 6.2 Data Fetch

Using Octokit with the job’s `GITHUB_TOKEN`:

* **Inline review comments**: `pulls.listReviewComments({ owner, repo, pull_number })`.
* **Reviews** (state + body): `pulls.listReviews({ owner, repo, pull_number })`.
* **Conversation comments**: `issues.listComments({ owner, repo, issue_number })`.
  Each returns paginated results; we fetch recent pages until we reach a safe cap (e.g., 250 items). ([GitHub Docs][4])

### 6.3 Normalization (privacy)

* Drop trivial/noisy items (e.g., “LGTM”, emoji‑only).
* **Trim fenced code blocks** inside comment bodies to ≤ 200 chars per block.
* Keep `{type: 'inline'|'review'|'conversation', body, path?, author_login, created_at}`.

### 6.4 OIDC Token

* Request: `const idToken = await core.getIDToken(core.getInput('oidc-audience') || 'skilllens.dev')`.
* Include in header `Authorization: Bearer <idToken>` to your Proxy.
* GitHub documents getting OIDC ID tokens using the Actions Toolkit. **The workflow must grant `id-token: write`.** ([GitHub Docs][5])

### 6.5 Call Proxy

* POST `${skilllens-api-url}` with JSON body:

  ```json
  {
    "repo": {"owner":"...","name":"...","prNumber":123},
    "reviews":[ { "type":"inline","body":"...", "path":"...", "author":"...", "created_at":"..." } ],
    "defaults":{"language":"Python","maxTopics":5,"minConfidence":0.65}
  }
  ```
* Expect `200 OK` with `{ topics: [...], commentMarkdown: "..." }` or `4xx/5xx` with an error object.

### 6.6 Upsert PR Comment

* Search for an existing comment containing `comment-marker` (default `<!-- SkillLens:v0 -->`) using `issues.listComments`.
* If found: `issues.updateComment(...)`; else: `issues.createComment(...)`. PR timeline comments are created via the **Issues API**. ([GitHub Docs][6])

### 6.7 Error Handling

* If the proxy responds with a non‑2xx:

  * If `fail-on-proxy-error == "true"`, `core.setFailed(...)`.
  * Else `core.warning(...)` and exit successfully (no comment).
* If `topics[]` is empty or `commentMarkdown` is blank → do nothing (no noise).

### 6.8 Outputs

* `topics-json`: JSON.stringify of topics (capped length).
* `comment-url`: Construct `https://github.com/${owner}/${repo}/pull/${prNumber}#issuecomment-${id}` if an update/create occurs.

---

## 7) `src/main.ts` (Skeleton)

````ts
import * as core from '@actions/core';
import * as github from '@actions/github';

type ReviewItem = {
  type: 'inline'|'review'|'conversation';
  body: string;
  path?: string;
  author?: string;
  created_at: string;
};

async function listData(octokit: ReturnType<typeof github.getOctokit>, owner: string, repo: string, pr: number) {
  const [inline, reviews, convo] = await Promise.all([
    octokit.rest.pulls.listReviewComments({ owner, repo, pull_number: pr, per_page: 100 }),
    octokit.rest.pulls.listReviews({ owner, repo, pull_number: pr, per_page: 100 }),
    octokit.rest.issues.listComments({ owner, repo, issue_number: pr, per_page: 100 })
  ]);

  const items: ReviewItem[] = [];
  // map each response into normalized items (trim code fences, drop noise)
  return items;
}

function redactCodeFences(body: string, max = 200): string {
  // replace ```...``` blocks with trimmed content
  return body.replace(/```([\s\S]*?)```/g, (_m, p1) => {
    const s = String(p1);
    return '```' + (s.length > max ? s.slice(0, max) + '…' : s) + '```';
  });
}

async function upsertComment(octokit: ReturnType<typeof github.getOctokit>, owner: string, repo: string, pr: number, marker: string, markdown: string) {
  const existing = await octokit.rest.issues.listComments({ owner, repo, issue_number: pr, per_page: 100 });
  const found = existing.data.find(c => c.body?.includes(marker));
  if (found) {
    await octokit.rest.issues.updateComment({ owner, repo, comment_id: found.id, body: markdown });
    return found.html_url;
  } else {
    const created = await octokit.rest.issues.createComment({ owner, repo, issue_number: pr, body: markdown });
    return created.data.html_url;
  }
}

async function run() {
  try {
    const {owner, repo} = github.context.repo;
    const pr = github.context.payload.pull_request?.number ?? github.context.payload.issue?.number;
    if (!pr) {
      core.info('No PR number found in context; exiting.');
      return;
    }

    const token = process.env.GITHUB_TOKEN || core.getInput('github-token'); // optional input if you prefer
    const octokit = github.getOctokit(token!);

    const items = await listData(octokit, owner, repo, pr);
    if (items.length === 0) {
      core.info('No review content to analyze; exiting.');
      return;
    }

    const apiUrl = core.getInput('skilllens-api-url', { required: true });
    const audience = core.getInput('oidc-audience') || 'skilllens.dev';
    const idToken = await core.getIDToken(audience);

    const defaults = {
      language: core.getInput('default-language') || 'Python',
      maxTopics: Number(core.getInput('max-topics') || '5'),
      minConfidence: Number(core.getInput('min-confidence') || '0.65')
    };

    const resp = await fetch(apiUrl, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${idToken}` },
      body: JSON.stringify({ repo: { owner, name: repo, prNumber: pr }, reviews: items, defaults })
    });

    if (!resp.ok) {
      const fail = core.getInput('fail-on-proxy-error') === 'true';
      const msg = `Proxy error ${resp.status}: ${await resp.text()}`;
      if (fail) return core.setFailed(msg);
      core.warning(msg);
      return;
    }

    const data = await resp.json() as { topics: unknown[]; commentMarkdown: string };
    if (!data.commentMarkdown) {
      core.info('Proxy returned no commentMarkdown; nothing to post.');
      return;
    }

    const url = await upsertComment(octokit, owner, repo, pr, core.getInput('comment-marker') || '<!-- SkillLens:v0 -->', data.commentMarkdown);
    core.setOutput('topics-json', JSON.stringify(data.topics ?? []));
    core.setOutput('comment-url', url);
  } catch (err) {
    core.setFailed(err instanceof Error ? err.message : String(err));
  }
}

run();
````

---

## 8) Distribution & Versioning

* **Bundle** to `dist/index.js` (Rollup via template scripts), **commit it**, and push. GitHub recommends bundling to avoid shipping `node_modules` to consumers. ([GitHub Docs][7])
* Create release tags like `v1.0.0` and move a **major tag** `v1` to the latest stable release so consumers can pin `@v1`. The template README includes guidance and a “Check dist/” workflow that ensures `dist` is up‑to‑date. ([GitHub][8])

---

## 9) Testing

* **Unit tests** with Jest (template already wired).
* **Local debug** with `@github/local-action` (template docs show how to run a local action against `.env`). ([GitHub][8])
* **Integration**: mock Octokit responses; mock Proxy with a minimal http server.
* **E2E**: run the Action in a test repo; verify a single comment is created and later **updated**, not duplicated.

---

## 10) Security & Permissions

* Action consumers must grant:

  ```yaml
  permissions:
    contents: read
    pull-requests: write
    issues: write
    id-token: write
  ```

  * `GITHUB_TOKEN` is recommended for GitHub API calls from workflows; permissions are scoped via `permissions:`. ([GitHub Docs][1])
  * OIDC is requested via `core.getIDToken(aud)`, per GitHub’s OIDC docs. ([GitHub Docs][5])

* **Data policy** in README:

  * Reads **review comments only**; does not read repository source code.
  * Sends only normalized review text (trimmed) to the Proxy.
  * Posts at most **one** bot comment per PR (idempotent via marker).

---

## 11) Error Cases & Behavior

| Case                               | Behavior                                                   |
| ---------------------------------- | ---------------------------------------------------------- |
| No PR number in context            | `core.info` + exit                                         |
| No review comments/reviews found   | Exit quietly (no comment)                                  |
| OIDC token request fails           | `core.setFailed`                                           |
| Proxy 4xx/5xx                      | Warn by default; `setFailed` if `fail-on-proxy-error=true` |
| Proxy returns empty topics/comment | Exit quietly                                               |
| Comment posting fails              | `core.setFailed` (critical path)                           |

---

## 12) README (consumer‑facing essentials)

* What it does (reviews → Hyperskill links).
* **Inputs** table (as in §4.2).
* **Permissions** block for workflows (as in §10).
* Example **workflow** usage (as in §5.1).
* Privacy note (no source code analysis).
* Limitations (max comments scanned, confidence threshold).
* Support/Issues link.

---

## 13) References

* **Create a JavaScript/TypeScript Action** — official tutorial (metadata, toolkit, bundling). ([GitHub Docs][7])
* **Template:** `actions/typescript-action` (build, tests, dist, release guidance). ([GitHub][8])
* **REST APIs used:** Pull Request **review comments**, **reviews**, **issue comments (PR)**. ([GitHub Docs][4])
* **GITHUB_TOKEN usage & permissions** (recommendation to use built‑in token). ([GitHub Docs][1])
* **OIDC from Actions** (requesting ID token from toolkit). ([GitHub Docs][5])

---

```
::contentReference[oaicite:25]{index=25}
```

[1]: https://docs.github.com/enterprise-cloud%40latest/rest/guides/scripting-with-the-rest-api-and-javascript?utm_source=chatgpt.com "Scripting with the REST API and JavaScript"
[2]: https://docs.github.com/en/rest/pulls/reviews?utm_source=chatgpt.com "REST API endpoints for pull request reviews"
[3]: https://docs.github.com/actions/security-guides/automatic-token-authentication?utm_source=chatgpt.com "Automatic token authentication"
[4]: https://docs.github.com/en/rest/pulls/comments?utm_source=chatgpt.com "REST API endpoints for pull request review comments"
[5]: https://docs.github.com/actions/security-for-github-actions/security-hardening-your-deployments/configuring-openid-connect-in-cloud-providers?utm_source=chatgpt.com "Configuring OpenID Connect in cloud providers"
[6]: https://docs.github.com/en/rest/issues/comments?utm_source=chatgpt.com "REST API endpoints for issue comments"
[7]: https://docs.github.com/actions/tutorials/creating-a-javascript-action "Creating a JavaScript action - GitHub Docs"
[8]: https://github.com/actions/typescript-action "GitHub - actions/typescript-action: Create a TypeScript Action with tests, linting, workflow, publishing, and versioning"
