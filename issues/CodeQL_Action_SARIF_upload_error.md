### CodeQL Action SARIF Upload Error

**Error Message:**  
The CodeQL Action does not support uploading multiple SARIF runs with the same category. Please update your workflow to upload a single run per category.

**Steps to Reproduce:**  
1. Run the CI workflow as in [this link](https://github.com/hyperskill/mobile-app/actions/runs/17767375166/job/50494087212).

**Cause:**  
This issue occurs because the `upload-sarif` step in the `ktlint` job tries to upload the whole `shared/build/reports/ktlint` directory, which can contain multiple SARIF files for the same category.

**Suggested Solution:**  
Modify the workflow to upload only a single SARIF file per category (e.g., `ktlintCheck.sarif`), or merge all relevant SARIF files into one before uploading.  
For more details, see the workflow at commit `6be66e90e45938aebb286e88486039aa02915c6d`.