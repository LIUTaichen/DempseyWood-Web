var map = L.map('mapid').setView([-36.914827,174.8072903], 13);
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiamFzb25saXV0YWljaGVuIiwiYSI6ImNqNmZ5ZGpkcjAzYWIzNXA1aWs5OXF3bXcifQ.33nis-JWUXYo1jpJkr1OSQ', {
		maxZoom: 18,
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'mapbox.streets'
	}).addTo(map);
//alert(coordinates.length);
var latlngs = [
        [[45.51, -122.68],
         [37.77, -122.43],
         [34.04, -118.2]],
        [[40.78, -73.91],
         [41.83, -87.62],
         [32.76, -96.72]]
    ];
var polyline = L.polyline(coordinates, {color: 'red'}).addTo(map);
//var polyline = L.polyline(latlngs, {color: 'red'}).addTo(mymap);
// zoom the map to the polyline
map.fitBounds(polyline.getBounds());

var drawnItems = new L.FeatureGroup();
     map.addLayer(drawnItems);
     var drawControl = new L.Control.Draw({
         draw: {
                  polyline: false,
                  rectangle: false,
                  circle: false,
                  marker: false
                },
         edit: {
             featureGroup: drawnItems
         }
     });
     map.addControl(drawControl);


  map.on('draw:edited', function (e) {
      var layers = e.layers;
      layers.eachLayer(function (layer) {
          //do whatever you want; most likely save back to db
      });
  });


   function display(data){
   console.log(data);
   alert(data);
   }

   map.on('draw:created', function (e) {
     var type = e.layerType;
     var layer = e.layer;
      if (type === 'marker') {
          // Do marker specific actions
      }else if(type === 'polygon'){
          var latLngs = layer.getLatLngs();
          var getJson = layer.toGeoJSON();
            var latJson = JSON.stringify(latLngs[0]);
             postLats(latLngs[0], display);
          }
          alert("finished");
      // Do whatever else you need to. (save to db; add to map etc)
      map.addLayer(layer);
   });

   function postLats(lats, callbackfn){
    $.ajax({
                           type: "POST",
                           url: "/geofence",
                           data: JSON.stringify(lats),
                           contentType: "application/json; charset=utf-8",
                           dataType: "json",

                           success: function(data){
                           callbackfn(data);

                           },
                           failure: function(errMsg) {
                              callbackfn(errMsg);//handle it in a proper way
                           }
                       });
   }
