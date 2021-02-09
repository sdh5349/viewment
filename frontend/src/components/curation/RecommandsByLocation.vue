<template>
  <div
    id="map"
    class="map"
  >
  </div>
</template>

<script>

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'RecommandsByLocation',
  data() {
    return {
      loading: true,
      map: null,
      lat: 0,
      lon: 0,
    }
  },
  created() {

  },
  mounted() {
    window.kakao && window.kakao.maps ? this.initMap() : this.addKakaoMapScript();
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
      // 사용자의 현재 위치를 기반으로 맵 보여준다.
      if (navigator.geolocation) {
        // GeoLocation을 이용해서 접속 위치를 얻어옵니다
        const success = function (pos) {
          const requestOptions = { 
            center: new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude),
            level: 4
          }
          const container = document.getElementById("map")
          self.map = new kakao.maps.Map(container, requestOptions)
        }

        const error = function (err) {
          console.warn('ERROR(' + err.code + '): ' + err.message);
        }

        navigator.geolocation.getCurrentPosition(success, error, {
          enableHighAccuracy: true,
          timeout: 5000,
          maximumAge: 0
        })
      } else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
        const options = { 
          center: new kakao.maps.LatLng(36.3551966493045, 127.298384348217),
          level: 4
        }
        const container = document.getElementById("map")
        self.map = new kakao.maps.Map(container, options)
      }
      
      this.createArticleMarker()
    },
    createArticleMarker() {
      var imageSrc = require('@/assets/images/pin.png'); 
    
      // for (var i = 0; i < positions.length; i ++) {
          
      //   // 마커 이미지의 이미지 크기 입니다
      //   var imageSize = new kakao.maps.Size(24, 35); 
        
      //   // 마커 이미지를 생성합니다    
      //   var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
        
      //   // 마커를 생성합니다
      //   var marker = new kakao.maps.Marker({
      //       map: map, // 마커를 표시할 지도
      //       position: positions[i].latlng, // 마커를 표시할 위치
      //       title : positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
      //       image : markerImage // 마커 이미지 
      //   });

      //   kakao.maps.event.addListener(marker, 'click', makeOverListener(map, marker, onArticleMarker(item.articleId))); 
      // } // for loop 끝
      
    },
    onArticleMarker(articleId) {
    },
  },
}
</script>

<style scoped>
  .map {
    width: 100%;
    height: 15rem;
    z-index: 0;
  }
</style>