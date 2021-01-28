<template>
  <v-container fluid>
    <div>{{ search }}</div>
    <div>안나와</div>
    <v-row dense>
      <v-col
        v-for="hashtag in hashtags"
        :key="hashtag.hid"
        :cols="hashtag.flex"
      >
        <v-card>
          <v-img
            :src="hashtag.img"
            class="white--text align-end"
            gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
            height="100px"
            width="100px"
          >
            <!-- <v-card-title v-text="hashtag.hid"></v-card-title> -->
          </v-img>

          <!-- <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn icon>
              <v-icon>mdi-heart</v-icon>
            </v-btn>

            <v-btn icon>
              <v-icon>mdi-bookmark</v-icon>
            </v-btn>

            <v-btn icon>
              <v-icon>mdi-share-variant</v-icon>
            </v-btn>
          </v-card-actions> -->
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    hashtags: [
        {
          "hid": 0,
          "contents": "에버랜드",
          "img": "https://upload.wikimedia.org/wikipedia/commons/thumb/6/63/Korea-Yongin-Everland-15.jpg/1200px-Korea-Yongin-Everland-15.jpg"
        },
        {
          "hid": 1,
          "contents": "에버랜드",
          "img": "https://res.klook.com/images/fl_lossy.progressive,q_65/c_fill,w_1200,h_630,f_auto/w_80,x_15,y_15,g_south_west,l_klook_water/activities/cm4zxn2rsf8vdfyla6sa/%EC%84%9C%EC%9A%B8%20-%20%EC%97%90%EB%B2%84%EB%9E%9C%EB%93%9C%20%EC%85%94%ED%8B%80%EB%B2%84%EC%8A%A4%20(%EB%8C%80%EC%84%B1%20%EA%B4%80%EA%B4%91).jpg"
        },
        {
          "hid": 2,
          "contents": "에버랜드",
          "img": "https://img3.tmon.kr/cdn3/deals/2020/04/29/3481785982/original_3481785982_front_7d638_1588148772production.jpg"
        },
    ],
  }),
  props: {
    search : {
      type: String,
    }
  }
}
</script>

<style>

</style>