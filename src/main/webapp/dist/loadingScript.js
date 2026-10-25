/**
 * 
 */
// Use the load event on the window object
window.addEventListener('load', function() {
    // Get the loader and content elements
    
    const loaderWrapper = document.getElementById('loader-wrapper');
    const content = document.getElementById('lodingcontent');

    // Hide the loader wrapper
    loaderWrapper.style.display = 'none';

    // Show the main content
    content.style.display = 'block';

    // Optional: add a class to body to manage overflow while loading
    document.body.style.overflow = 'auto'; 
});