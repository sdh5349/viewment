<template>
<v-container>
  
  <v-row
    justify="center"
  >
    <v-text-field
    text-center
    hide-details
    prepend-icon="mdi-magnify"
    single-line
    append-icon="mdi-close"
    @clear:append="myValue = 1"
    label="search"
    >
    </v-text-field>
  </v-row>
  <v-row
    justify="center"
    row wrap
  >
    <v-col>
      <div class="text-center">
        <v-btn
          icon
          @click="goMap"
        >
          <v-icon>
            mdi-map
          </v-icon>
        </v-btn>
      </div>
    </v-col>

    <v-col>
      <div class="text-center">
        <v-btn
          icon
          @click="goHashtag"
        >
          <v-icon>
            mdi-post
          </v-icon>
        </v-btn>
      </div>
    </v-col>

    <v-col>
      <div class="text-center">
        <v-btn
          icon
          @click="goUser"
        >
          <v-icon>
            mdi-account
          </v-icon>
        </v-btn>
      </div>
    </v-col>
  </v-row>
  <v-row>
    <div>

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

export default {
  name: 'Search',
  components: {
    SearchLocation,   
    SearchHashtag,
    SearchUser,   
  },
  data: () => ({
    showMap: false,
    showHashtag: false,
    showAccount: false
  }),
  methods: {
    goMap() {
      console.log('Map')
      this.showMap = true
      this.showHashtag = false
      this.showAccount = false
    },
    goHashtag() {
      console.log('Hashtag')
      this.showMap = false
      this.showHashtag = true
      this.showAccount = false
    },
    goUser() {
      console.log('User')
      this.showMap = false
      this.showHashtag = false
      this.showAccount = true
    },
  }
}
</script>

<style>

</style>