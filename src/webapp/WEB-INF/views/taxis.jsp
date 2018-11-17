<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta name="viewport" content="initial-scale=1.0, width=device-width"/>
    <script type="text/javascript" src="https://js.api.here.com/v3/3.0/mapsjs-core.js"></script>
    <script type="text/javascript" src="https://js.api.here.com/v3/3.0/mapsjs-service.js"></script>
    <script type="text/javascript" src="https://js.api.here.com/v3/3.0/mapsjs-ui.js"></script>
    <script type="text/javascript" src="https://js.api.here.com/v3/3.0/mapsjs-mapevents.js"></script>

</head>
<body>
<div id="map" style="width: 100%; height: 400px; background: grey"></div>
<script th:inline="javascript">
    // Initialize the platform object:
    var platform = new H.service.Platform({
        'app_id': 'F7iYpiXSc8wnCRDYfMUQ',
        'app_code': 'jpDlJdgGk5ms7QQH-NvpUQ'
    });

    function moveMapToValue(map){
        map.setCenter({lat:[[${session.taxi.latitude}]], lng:[[${session.taxi.longtitude}]]});
        map.setZoom(14);
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
</script>

<form action="#" th:action="@{/route}" th:object="${taxi}" method="post">
    <p>Address: <input type="text" th:field="*{address}"/></p>
    <p><input type="submit" value="Submit"/>
    </p>
</form>

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

</body>
</html>