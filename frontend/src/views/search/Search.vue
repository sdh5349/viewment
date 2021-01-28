<template>
<v-container>
  
  <v-row
    justify="center"
  >
    <v-autocomplete
      prepend-icon="mdi-magnify"
      v-model="select"
      :loading="loading"
      :items="items"
      :search-input.sync="search"
      cache-items
      class="mx-4"
      flat
      hide-no-data
      hide-details
      clearable
      auto-select-first
      @change="onSearch(select)"
    >
      <template v-slot:label>
       {{searchLabel}}
      </template>
    </v-autocomplete>
  </v-row>
  <v-row
    justify="center"
    row wrap
  >
  <!-- 탭 -->
    <v-tabs
      v-model="onTab"
      grow
    >
      <v-tab
        v-for="tabItem in tabItems"
        :key="tabItem.tabId"
        :disabled="!onTab"
      >
        <v-icon>{{ tabItem.icon }}</v-icon>
      </v-tab>
    </v-tabs>
  <!-- 탭 -->

  <!-- 탭-컴포넌트 -->
    <v-tabs-items
      v-model="onTab"
      continuous
      default
    >
      <v-tab-item
        v-for="tabItem in tabItems"
        :key="tabItem.tabId"
        disabled
      >
        <v-card flat>
          <component 
            v-bind:is="tabItem.content"

          ></component>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
    <!-- 탭-컴포넌트 -->
  </v-row>
</v-container>
</template>

<script>
import SearchLocation from '@/components/search/SearchLocation'
import SearchHashtag from '@/components/search/SearchHashtag'
import SearchUser from '@/components/search/SearchUser'
import SearchRecord from '@/components/search/SearchRecord'
import axios from 'axios'

export default {
  name: 'Search',
  components: {
    SearchRecord,
    SearchLocation,   
    SearchHashtag,
    SearchUser,   
  },
  data () {
    return {
      onTab: null,
      tabItems: [
        { tabId: 0, tabName: 'Map', icon: 'mdi-map', content: 'SearchLocation' },
        { tabId: 1, tabName: 'Feed', icon: 'mdi-post', content: 'SearchHashtag' },
        { tabId: 2, tabName: 'User', icon: 'mdi-account', content: 'SearchUser' }
      ],
      loading: false,
      items: [],
      search: null,
      select: null,
      users: [
        '재혁',
        '동현',
        '동규',
        '종성',
        '주희'
      ],
      locations: [
        'Alabama',
        'Alaska',
        'American Samoa',
        'Arizona',
        'Arkansas',
        'California',
        'Colorado',
        'Connecticut',
        'Delaware',
      ],
      hashtags: [
        '에버랜드',
        '롯데월드',
        '오월드',
        '이월드',
      ],
      searchLabel : "Search",
    }
  },
  methods: {
    onSearch(select) {
      console.log(select)
      
    },
    setToken() {
      const token = localStorage.getItem('jwt')
      const config = {
        headers: {
          Authoriztion: `JWT ${token}`
        }
      }
      return config
    },
    async getSearches() {
      const config = this.setToken()
      // map api 받아오기
      const response1 = await axios.get('', config)
      console.log(response1)
      // hashtag api 받아오기
      const response2 = await axios.get('', config)
      console.log(response2)
      // user api 받아오기
      const response3 = await axios.get('', config)
      console.log(response3)

    },
    querySelections (v) {
      this.loading = true
      // Simulated ajax query
      setTimeout(() => {
        if (this.onTab === 2){
          this.items = this.users.filter(e => {
            return (e || '').toLowerCase().indexOf((v || '').toLowerCase()) > -1
          })
        }
        else if (this.onTab === 0){
          this.items = this.locations.filter(e => {
            return (e || '').toLowerCase().indexOf((v || '').toLowerCase()) > -1
          })
        }
        else if (this.onTab === 1){
          this.items = this.hashtags.filter(e => {
            return (e || '').toLowerCase().indexOf((v || '').toLowerCase()) > -1
          })
        }
        this.loading = false
      }, 500)
    },
  },
  created() {
    // const token = sessionStorage.getItem('jwt')
    // if (token) {
      // this.getSearches()
    // }
  },
  watch: {
    search (val) {
      val && val !== this.select && this.querySelections(val)
    },
  },
}
</script>

<style>

</style>