addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('pre code')
    .forEach(codeBlock => {
        codeBlock.innerHTML = codeBlock.innerHTML
        .split(/\r?\n/)
        .map(line => `<span>${line}</span>`)
        .join('\n')
    })
});
