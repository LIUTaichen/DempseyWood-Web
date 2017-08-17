var mymap = L.map('mapid').setView([-36.914827,174.8072903], 13);
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiamFzb25saXV0YWljaGVuIiwiYSI6ImNqNmZ5ZGpkcjAzYWIzNXA1aWs5OXF3bXcifQ.33nis-JWUXYo1jpJkr1OSQ', {
		maxZoom: 18,
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'mapbox.streets'
	}).addTo(mymap);
//alert(coordinates.length);
var latlngs = [
        [[45.51, -122.68],
         [37.77, -122.43],
         [34.04, -118.2]],
        [[40.78, -73.91],
         [41.83, -87.62],
         [32.76, -96.72]]
    ];
var polyline = L.polyline(coordinates, {color: 'red'}).addTo(mymap);
//var polyline = L.polyline(latlngs, {color: 'red'}).addTo(mymap);
// zoom the map to the polyline
mymap.fitBounds(polyline.getBounds());