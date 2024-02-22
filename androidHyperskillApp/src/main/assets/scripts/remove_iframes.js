addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('iframe')
        .forEach(element =>
            element.parentNode.removeChild(element)
        );
});