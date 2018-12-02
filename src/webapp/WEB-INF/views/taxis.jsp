<!--Andreas Kramer-->

<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta name="viewport" content="initial-scale=1.0, width=device-width"/>
    <script type="text/javascript" src="https://js.api.here.com/v3/3.0/mapsjs-core.js"></script>
    <script type="text/javascript" src="https://js.api.here.com/v3/3.0/mapsjs-service.js"></script>
    <script type="text/javascript" src="https://js.api.here.com/v3/3.0/mapsjs-ui.js"></script>
    <script type="text/javascript" src="https://js.api.here.com/v3/3.0/mapsjs-mapevents.js"></script>
    <style type="text/css">

        .directions li span.arrow {
            display: inline-block;
            min-width: 28px;
            min-height: 28px;
            background-position: 0px;
            background-image: url("../img/arrows.png");
            position: relative;
            top: 8px;
        }

        .directions li span.depart {
            background-position: -28px;
        }

        .directions li span.rightUTurn {
            background-position: -56px;
        }

        .directions li span.leftUTurn {
            background-position: -84px;
        }

        .directions li span.rightFork {
            background-position: -112px;
        }

        .directions li span.leftFork {
            background-position: -140px;
        }

        .directions li span.rightMerge {
            background-position: -112px;
        }

        .directions li span.leftMerge {
            background-position: -140px;
        }

        .directions li span.slightRightTurn {
            background-position: -168px;
        }

        .directions li span.slightLeftTurn {
            background-position: -196px;
        }

        .directions li span.rightTurn {
            background-position: -224px;
        }

        .directions li span.leftTurn {
            background-position: -252px;
        }

        .directions li span.sharpRightTurn {
            background-position: -280px;
        }

        .directions li span.sharpLeftTurn {
            background-position: -308px;
        }

        .directions li span.rightRoundaboutExit1 {
            background-position: -616px;
        }

        .directions li span.rightRoundaboutExit2 {
            background-position: -644px;
        }

        .directions li span.rightRoundaboutExit3 {
            background-position: -672px;
        }

        .directions li span.rightRoundaboutExit4 {
            background-position: -700px;
        }

        .directions li span.rightRoundaboutPass {
            background-position: -700px;
        }

        .directions li span.rightRoundaboutExit5 {
            background-position: -728px;
        }

        .directions li span.rightRoundaboutExit6 {
            background-position: -756px;
        }

        .directions li span.rightRoundaboutExit7 {
            background-position: -784px;
        }

        .directions li span.rightRoundaboutExit8 {
            background-position: -812px;
        }

        .directions li span.rightRoundaboutExit9 {
            background-position: -840px;
        }

        .directions li span.rightRoundaboutExit10 {
            background-position: -868px;
        }

        .directions li span.rightRoundaboutExit11 {
            background-position: 896px;
        }

        .directions li span.rightRoundaboutExit12 {
            background-position: 924px;
        }

        .directions li span.leftRoundaboutExit1 {
            background-position: -952px;
        }

        .directions li span.leftRoundaboutExit2 {
            background-position: -980px;
        }

        .directions li span.leftRoundaboutExit3 {
            background-position: -1008px;
        }

        .directions li span.leftRoundaboutExit4 {
            background-position: -1036px;
        }

        .directions li span.leftRoundaboutPass {
            background-position: 1036px;
        }

        .directions li span.leftRoundaboutExit5 {
            background-position: -1064px;
        }

        .directions li span.leftRoundaboutExit6 {
            background-position: -1092px;
        }

        .directions li span.leftRoundaboutExit7 {
            background-position: -1120px;
        }

        .directions li span.leftRoundaboutExit8 {
            background-position: -1148px;
        }

        .directions li span.leftRoundaboutExit9 {
            background-position: -1176px;
        }

        .directions li span.leftRoundaboutExit10 {
            background-position: -1204px;
        }

        .directions li span.leftRoundaboutExit11 {
            background-position: -1232px;
        }

        .directions li span.leftRoundaboutExit12 {
            background-position: -1260px;
        }

        .directions li span.arrive {
            background-position: -1288px;
        }

        .directions li span.leftRamp {
            background-position: -392px;
        }

        .directions li span.rightRamp {
            background-position: -420px;
        }

        .directions li span.leftExit {
            background-position: -448px;
        }

        .directions li span.rightExit {
            background-position: -476px;
        }

        .directions li span.ferry {
            background-position: -1316px;
        }
    </style>


</head>
<body>
<div id="map" style="width: 100%; height: 400px; background: grey"></div>
<script th:inline="javascript">
    // Initialize the platform object:
    var platform = new H.service.Platform({
        'app_id': 'F7iYpiXSc8wnCRDYfMUQ',
        'app_code': 'jpDlJdgGk5ms7QQH-NvpUQ'
    });

    function moveMapToValue(map) {
        map.setCenter({lat: [[${session.taxi.latitude}]], lng: [[${session.taxi.longtitude}]]});
        map.setZoom(14);

        var startMarker = new H.map.Marker({
            lat: [[${session.taxi.latitude}]],
            lng: [[${session.taxi.longtitude}]]
        });
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

    if ([[${session.taxi.route}]] != null) {
        var route = [[${session.taxi.route}]].response.route[0];

        addRouteShapeToMap(route);
        addManueversToMap(route);
    }

</script>

<form action="#" th:action="@{/route}" th:object="${taxi}" method="post">
    <p>Startadresse: <input type="text" th:field="*{address}"/></p>
    <input type="datetime-local" placeholder="" th:value="*{estimatedTime}" th:field="*{estimatedTime}"/>
    <p><input type="submit" value="Suchen"/>
    </p>
</form>

<form action="#" th:action="@{/calcRoute}" th:object="${taxi}" method="post">
    <p>Zieladresse: <input type="text" th:field="*{endPoint}"/></p>
    <p><input type="submit" value="Route berechnen"/>
    </p>
</form>
<!--
<table>
    <thead>
    <tr>
        <th>Address</th>
        <th>Estimated time</th>
        <th>Status</th>
        <th>Latitude</th>
        <th>Longitude</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td th:text="${taxi.address}"></td>
        <td th:text="${taxi.estimatedTime}"></td>
        <td th:text="${taxi.status}"></td>
        <td th:text="${taxi.latitude}"></td>
        <td th:text="${taxi.longtitude}"></td>
    </tr>
    </tbody>
</table>
-->
<span th:if="${routeInfo != null && routeInfo.summary != null}">
<p th:utext="${routeInfo.summary.text}"></p>
    <p th:text="${taxi.status}"></p>

    <table>
    <thead>
    <tr>
        <th>Zusammenfassung</th>
        <th>Latitude</th>
        <th>Longitude</th>
    </tr>
    </thead>
    <tbody>
    <tr th:each="point : ${routeInfo.maneuver}">
        <td th:utext="${point.instruction}"></td>
        <td th:text="${point.position.latitude}"></td>
        <td th:text="${point.position.longitude}"></td>
    </tr>
    </tbody>
</table>
</span>
<button>
    <a href="/">Back</a></button>
</body>
</html>