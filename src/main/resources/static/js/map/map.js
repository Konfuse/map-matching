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
    url: "/geo",
    data: {},
    success: function (data) {
        var poi = JSON.parse(data);
        // console.log("SUCCESS : ", data);
        L.geoJson(poi, {
            style: function(feature) {
                var color;
                switch (feature.properties.speed) {
                    case 'red':  color = "#f08080"; break;
                    case 'yellow':    color = "#f0e68c"; break;
                    case 'green':    color = "#98fb98";
                }
                return {
                    fillColor: color,
                    weight: 2,
                    opacity: 1,
                    color: 'white',
                    dashArray: '3',
                    fillOpacity: 0.7
                };
            }
        }).addTo(map);
            }
});

