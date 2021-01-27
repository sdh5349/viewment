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
    <v-tabs
      fixed-tabs
      color="cyan"
    >
      <v-tab
        @click="goMap"
      >
        <v-icon>
          mdi-map
        </v-icon>
      </v-tab>
      <v-tab
         @click="goHashtag"
      >
        <v-icon>
          mdi-post
        </v-icon>
      </v-tab>
      <v-tab
        @click="goUser"
      >
        <v-icon>
          mdi-account
        </v-icon> 
      </v-tab>
    </v-tabs>
  </v-row>
  <v-row>
    <div v-if="showSearchlog">
      <SearchRecord />
    </div>
    <div v-if="showMap">
      <SearchLocation />
    </div>
    <div v-if="showHashtag">
      <SearchHashtag />
    </div>
    <div v-if="showAccount">
      <SearchUser />
    </div>
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
      loading: false,
      items: [],
      search: null,
      select: null,
      states: [
        '재혁',
        '동현',
        '동규',
        '종성',
        '주희',
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
      showSearchlog: false,
      showMap: false,
      showHashtag: false,
      showAccount: false,
      searchLabel : "Search",
    }
  },
  methods: {
    showLog() {
      this.searchLabel = "Search"
      this.showSearchlog = true
      this.showMap = false
      this.showHashtag = false
      this.showAccount = false
    },
    goMap() {
      this.searchLabel = "Map Search"
      this.showSearchlog = false
      this.showMap = true
      this.showHashtag = false
      this.showAccount = false
    },
    goHashtag() {
      this.searchLabel = "Hashtag Search"
      this.showSearchlog = false
      this.showMap = false
      this.showHashtag = true
      this.showAccount = false
    },
    goUser() {
      this.searchLabel = "User Search"
      this.showSearchlog = false
      this.showMap = false
      this.showHashtag = false
      this.showAccount = true
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
    getSearchlog() {
      const config = this.setToken()
      axios.get('', config)
      .then(res => {
        console.log(res)
      })
      .catch(err => {
        console.log(err)
      })
    },
    querySelections (v) {
      this.loading = true
      // Simulated ajax query
      setTimeout(() => {
        this.items = this.states.filter(e => {
          return (e || '').toLowerCase().indexOf((v || '').toLowerCase()) > -1
        })
        this.loading = false
      }, 500)
    },
  },
  created() {
    // const token = sessionStorage.getItem('jwt')
    // if (token) {
      this.showLog()
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