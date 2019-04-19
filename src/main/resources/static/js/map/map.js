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

$.ajax({
    type: "GET",
    url: "/circle",
    data: {},
    success: function (data) {
        var poi = JSON.parse(data);
        var circle = L.circle([poi.lat, poi.lng], 500, {
            color: 'red',
            fillColor: '#f03',
            fillOpacity: 0.5
        }).addTo(map);
        console.log("SUCCESS : ", data);
            }
});

