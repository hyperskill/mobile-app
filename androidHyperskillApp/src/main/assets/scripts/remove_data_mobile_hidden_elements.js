addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('[data-mobile-hidden="true"]')
        .forEach(element => {
            element.parentNode.removeChild(element)
        })
});