function setHalfCircleProgress(elementId, percentage) {
    const indicator = document.getElementById(elementId);
    const rotationAngle = (percentage / 100) * 180; // 180 degrees for 100%
    indicator.style.transform = `rotate(${rotationAngle}deg)`;
}
function updateHalfRing(widgetVar, value, color) {
    // value expected 0..100
    const el = document.querySelector('[data-widget="'+widgetVar+'"]');
    if(!el) return;
    const svgArc = el.querySelector('.progress-arc');
    const tip = el.querySelector('.tip-circle');
    const tipText = el.querySelector('.tip-text');
    // circle with radius r; arc length for half circle (180deg) = pi*r
    const r = parseFloat(svgArc.getAttribute('r'));
    const circumferenceHalf = Math.PI * r;
    const dash = (value/100) * circumferenceHalf;
    svgArc.style.strokeDasharray = dash + ' ' + (circumferenceHalf - dash);
    svgArc.style.stroke = color;

    // compute tip position along semicircle from left (180deg) to right (0deg)
    // map value 0->180deg, 100->0deg
    const angleDeg = 180 - (value/100)*180;
    const cx = parseFloat(svgArc.getAttribute('cx')), cy = parseFloat(svgArc.getAttribute('cy'));
    const angleRad = angleDeg * Math.PI/180;
    const tx = cx + r * Math.cos(angleRad);
    const ty = cy - r * Math.sin(angleRad);
    tip.setAttribute('cx', tx);
    tip.setAttribute('cy', ty);
    tip.setAttribute('fill', color);

    // place tip label slightly outward along radial direction
    const labelOffset = 14;
    const lx = cx + (r + labelOffset) * Math.cos(angleRad);
    const ly = cy - (r + labelOffset) * Math.sin(angleRad);
    tipText.setAttribute('x', lx);
    tipText.setAttribute('y', ly);
    tipText.textContent = Math.round(value) + '%';
}

async function inlineSvgFromImg(imgId, wrapperId) {
    const img = document.getElementById(imgId);
    if(!img) return;
    try {
        const url = img.src;
        const res = await fetch(url);
        const text = await res.text();
        const parser = new DOMParser();
        const doc = parser.parseFromString(text, 'image/svg+xml');
        const svg = doc.documentElement;
        svg.removeAttribute('width'); svg.removeAttribute('height'); // optional
        document.getElementById(wrapperId).innerHTML = '';
        document.getElementById(wrapperId).appendChild(svg);
        // now call setHalfRingProgress normally
    } catch(e) {
        console.error('Failed to inline SVG', e);
    }
}

document.addEventListener('DOMContentLoaded', function(){
    inlineSvgFromImg('halfSvgImg','svgWrapper');
});

// Example usage:
// setHalfCircleProgress('myProgressBarIndicator', 75); // Sets progress to 75%
