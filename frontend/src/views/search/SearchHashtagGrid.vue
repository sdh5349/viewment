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
      class="py-6"
    >
      <v-list-item three-line>
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
      </v-list-item>
    </v-card>    
    <!-- 프로필 카드 끝 -->
    <HashtagGrid
      :hash="hash"
    />
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
      loading: true,
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

<style>

</style>