function updateMap() {
    var geojson;

    $.ajax({
        type: "GET",
        url: "/geo",
        dataType: "json",
        data: {
            timestamp : $("#timestamp").val()
        },
        success: function (data) {
            console.log("SUCCESS : ", data);
            geojson = L.geoJson(data, {
                style: style,
                onEachFeature : onEachFeature
            });
            layerGroup.clearLayers();
            layerGroup.addLayer(geojson);
        }
    });

    function style(feature) {
        return {
            fillColor: getColor(feature.properties.speed),
            weight: 2,
            opacity: 1,
            color: 'white',
            dashArray: '3',
            fillOpacity: 0.7
        };
    }

    function getColor(speed) {
        return speed > 40 ? '#98fb98' : speed > 20 ? '#f0e68c' : '#f08080';
    }

    function highlightFeature(e) {
        var layer = e.target;
        layer.setStyle({
            weight: 5,
            color: '#666',
            dashArray: '',
            fillOpacity: 0.7
        });
        if (!L.Browser.ie && !L.Browser.opera) {
            layer.bringToFront();
        }
        info.update(layer.feature.properties);
    }

    function resetHighlight(e) {
        geojson.resetStyle(e.target);
        info.update();
    }

    function zoomToFeature(e) {
        map.fitBounds(e.target.getBounds());
    }

    function onEachFeature(feature, layer) {
        layer.on({
            mouseover: highlightFeature,
            mouseout: resetHighlight,
            click: zoomToFeature
        });
    }
}