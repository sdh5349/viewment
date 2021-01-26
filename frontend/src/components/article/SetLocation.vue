<template>
  <div>
    <v-text-field @keypress.enter='searchAddress' v-model="address"></v-text-field>
    <div id="map"  class="map" ></div>
    <v-btn @click="markerCheck">위치지정</v-btn>
    <p id="result"></p> 
  </div>
</template>

<script>
export default {
  data() {
    return{
      myLocation: '',
      address: '',
      markerInfo: ''
    }
  },
  mounted() {
    window.kakao && window.kakao.maps
      ? this.initMap()
      : this.addKakaoMapScript();
  },
  methods: {
    addKakaoMapScript() {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap)
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=41dd8e1c2fab039d8dbbff2e13e8d5a5";
      document.head.appendChild(script);
    },
    initMap() {
      const self = this
      var control = new kakao.maps.ZoomControl();
      var container = document.getElementById("map")
      this.$getLocation()
      .then(coordinates => {
        this.myLocation = coordinates
      })
      .then(() => {
        var options = {
          //지도를 생성할 때 필요한 기본 옵션
          center: new kakao.maps.LatLng(this.myLocation.lat, this.myLocation.lng), //지도의 중심좌표.
          level: 3 //지도의 레벨(확대, 축소 정도)
        }
        
        var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
        kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
          // 클릭한 위도, 경도 정보를 가져옵니다 
          var latlng = mouseEvent.latLng;
          var message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, '
          message += '경도는 ' + latlng.getLng() + ' 입니다'
          var resultDiv = document.getElementById('result')
          resultDiv.innerHTML = message
          const position = new kakao.maps.LatLng(latlng.getLat(), latlng.getLng())
          var marker = new kakao.maps.Marker({
            map: map,
            position: position
          })
          
          if (this.markerInfo==undefined){
            marker.setMap(map)
          } else {
            this.markerInfo.setMap(null)
            marker.setMap(map)
          }
          self.markerInfo = position
          })
      })
    },
    searchAddress() {
      
      var geocoder = new kakao.maps.services.Geocoder()
      var callback = function(result, status) {
          if (status === kakao.maps.services.Status.OK) {
              console.log(result);
          }
      }
    
      geocoder.addressSearch(this.address, function(result, status) {

    // 정상적으로 검색이 완료됐으면 
      if (status === kakao.maps.services.Status.OK) {

      var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
      

      var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
          mapOption = {
              center: new kakao.maps.LatLng(coords.Ma, coords.La), // 지도의 중심좌표
              level: 3 // 지도의 확대 레벨
          };  

      // 지도를 생성합니다    
      var map = new kakao.maps.Map(mapContainer, mapOption)
        map.setCenter(coords);
        
        kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
          // 클릭한 위도, 경도 정보를 가져옵니다 
          var latlng = mouseEvent.latLng;
          var message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, '
          message += '경도는 ' + latlng.getLng() + ' 입니다'
          var resultDiv = document.getElementById('result')
          resultDiv.innerHTML = message
          var marker = new kakao.maps.Marker({
            map: map,
            position: new kakao.maps.LatLng(latlng.getLat(), latlng.getLng())
          })
          
          if (this.markerInfo==undefined){
            marker.setMap(map)
          } else {
            this.markerInfo.setMap(null)
            marker.setMap(map)
          }
          this.markerInfo = marker    
          console.log(this.markerInfo)
          })
        } 
      })    
    },
    markerCheck() {
      this.$emit('onClick', this.markerInfo)
    }
  }
}
</script>

<style>
.map {
  width: 100%;
  height: 400px;
}
</style>