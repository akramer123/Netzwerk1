// Initialize the platform object:
var platform = new H.service.Platform({
    'app_id': 'F7iYpiXSc8wnCRDYfMUQ',
    'app_code': 'jpDlJdgGk5ms7QQH-NvpUQ'
});

function moveMapToValue(map) {
    map.setCenter({lat: [[${session.taxi.latitude}]], lng: [[${session.taxi.longtitude}]]});
    map.setZoom(14);

    var startMarker = new H.map.Marker({lat: [[${session.taxi.latitude}]], lng: [[${session.taxi.longtitude}]]});
    map.addObject(startMarker);

    // Show traffic tiles
    map.setBaseLayer(defaultLayers.normal.traffic);

    // Enable traffic incidents layer
    map.addLayer(defaultLayers.incidents);
}

function addRouteShapeToMap(route) {
    var lineString = new H.geo.LineString();
    var routeShape = route.shape;
    var polyline;

    routeShape.forEach(function (point) {
        var parts = point.split(',');
        lineString.pushLatLngAlt(parts[0], parts[1]);
    });

    polyline = new H.map.Polyline(lineString, {
        style: {
            lineWidth: 4,
            strokeColor: 'rgba(0, 128, 255, 0.7)'
        }
    });
    // Add the polyline to the map
    map.addObject(polyline);
    // And zoom to its bounding rectangle
    map.setViewBounds(polyline.getBounds(), true);
}


/**
 * Creates a series of H.map.Marker points from the route and adds them to the map.
 * @param {Object} route  A route as received from the H.service.RoutingService
 */
function addManueversToMap(route) {
    var svgMarkup = '<svg width="18" height="18" ' +
        'xmlns="http://www.w3.org/2000/svg">' +
        '<circle cx="8" cy="8" r="8" ' +
        'fill="#1b468d" stroke="white" stroke-width="1"  />' +
        '</svg>',
        dotIcon = new H.map.Icon(svgMarkup, {anchor: {x: 8, y: 8}}),
        group = new H.map.Group(),
        i,
        j;

    // Add a marker for each maneuver
    for (i = 0; i < route.leg.length; i += 1) {
        for (j = 0; j < route.leg[i].maneuver.length; j += 1) {
            // Get the next maneuver.
            maneuver = route.leg[i].maneuver[j];
            // Add a marker to the maneuvers group
            var marker = new H.map.Marker({
                    lat: maneuver.position.latitude,
                    lng: maneuver.position.longitude
                },
                {icon: dotIcon});
            marker.instruction = maneuver.instruction;
            group.addObject(marker);
        }
    }

    group.addEventListener('tap', function (evt) {
        map.setCenter(evt.target.getPosition());
        openBubble(
            evt.target.getPosition(), evt.target.instruction);
    }, false);

    // Add the maneuvers group to the map
    map.addObject(group);
}

var pixelRatio = window.devicePixelRatio || 1;
var defaultLayers = platform.createDefaultLayers({
    tileSize: pixelRatio === 1 ? 256 : 512,
    ppi: pixelRatio === 1 ? undefined : 320
});

//Step 2: initialize a map  - not specificing a location will give a whole world view.
var map = new H.Map(document.getElementById('map'),
    defaultLayers.normal.map, {pixelRatio: pixelRatio});

//Step 3: make the map interactive
// MapEvents enables the event system
// Behavior implements default interactions for pan/zoom (also on mobile touch environments)
var behavior = new H.mapevents.Behavior(new H.mapevents.MapEvents(map));

// Create the default UI components
var ui = H.ui.UI.createDefault(map, defaultLayers);

moveMapToValue(map);

var route = [[${session.taxi.route}]].response.route[0];
;
addRouteShapeToMap(route);
addManueversToMap(route);
