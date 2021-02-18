<template>
  <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
    
    <!-- 프로필 카드 시작 -->
    <v-card
      flat
    >
      <v-list-item three-line>
        <!-- <div class="relative-container">  -->
        <v-card
          elevation="0"
        >
        </v-card>

        <v-list-item-content
          class="ml-2"
        >
          <v-list-item-title
            align="left"
            class="headline mb-2"
          >
            # {{hash}}
          </v-list-item-title>
        </v-list-item-content>
        <!-- </div> -->
      </v-list-item>
    </v-card>    
    <!-- 프로필 카드 끝 -->
    <div class="relative-container">
      <HashtagGrid
        :hash="hash"
        class="mt-1 mx-0"
      />
    </div>
    </v-col>
  </v-row>
</template>

<script>
import HashtagGrid from '@/components/search/HashtagGrid'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'SearchHashtagGrid',
  components: {
    HashtagGrid,  
  },
  data() {
    return {
      hash: "",
    }
  },
  props:{
    clickedHash : String,
  },
  methods: {
    getParams() {
      this.hash = this.clickedHash
    }
  },
  created() {
    this.getParams()
  },
}
</script>

<style scoped>
/* 컨테이너를 relative position으로 바꾼다. */
.relative-container {
  position: relative
}
</style>