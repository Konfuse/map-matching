// Map initialize

var map = L.map( 'map', {
    center: [34.2660,108.9415],
    minZoom: 2,
    zoom: 12
});

L.tileLayer( 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.github.com">@孔维健</a>',
    subdomains: ['a','b','c']
}).addTo( map );

var info = L.control();
info.onAdd = function(map) {
    this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
    this.update();
    return this._div;
};
// method that we will use to update the control based on feature properties passed
info.update = function (props) {
    this._div.innerHTML = '<h4>Traffic Speed</h4>' +  (props ?
        '<b>' + props.status + '</b><br />' + props.speed + ' km / h'
        : 'Over Xian');
};
info.addTo(map);

layerGroup = L.layerGroup();
layerGroup.addTo(map);