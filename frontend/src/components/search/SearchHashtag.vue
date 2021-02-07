<template>
  <v-row
  justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
      <v-card
        class="mx-auto mt-5"
        flat
      >
        <v-list>
          <v-list-item
            v-for="hash in hashtags.slice(0,9)"
            :key="hash.contents"
            @click="goGrid(hash)"
          >
            <v-list-item-avatar>
              <v-icon>
                mdi-pound
              </v-icon>
            </v-list-item-avatar>

            <v-list-item-content>
              <v-list-item-title v-text="hash.contents"></v-list-item-title>
            </v-list-item-content>

          </v-list-item>
        </v-list>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  data() {
    return {
      hashtags: [],
      Historys: [],
    }
  },
  props: {
    search : {
      type: String,
    },
    onTab : {
      type: Number, 
    },
    limitMax: {
      type: Number,
    },
  },
  computed: {
    getToken() {
      const token = sessionStorage.getItem('jwt')

      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
  methods: {
    getHashtags() {
      if(this.search){
        axios.get(`${SERVER_URL}/hashtags/${this.search}`, this.getToken)
          .then((res) => {
          console.log(res) 
          console.log(res.data)
          this.hashtags = res.data      
          })
          .catch((err)=> {
            alert('error'+err.message)
          })
      }
    },
    goGrid(hash) {
      console.log(hash.contents)
      this.Historys = 
        {
          HistoryTitle: hash.contents,
          HistoryContent: hash.hashtagId, 
          HistoryIcon:"mdi-pound",
          HistoryProperty: "Hashtag",
        }
      this.appendToStorage(this.Historys)
    },
    appendToStorage(Historys) {
      var str = localStorage.getItem("Historys");
      var obj = {};
      var limitMax = 6;
      try {
        obj = JSON.parse(str);
      } catch {
        obj = {};
      }
      if(!obj){
        obj = {};
        obj["Historys"] = [];
      }
      obj["Historys"].push(Historys);
      if (limitMax && limitMax < obj["Historys"].length) {
        let tempList = [];
        for(let i = obj["Historys"].length-limitMax; i < obj["Historys"].length; i++) {
          tempList.push(obj["Historys"][i]);
        }
        obj["Historys"] = tempList;
      }
      localStorage.setItem("Historys", JSON.stringify(obj));
    },
  },
  watch: {
    onTab: {
      immediate: true,
      handler(onTab) {
        fetch(`/${onTab}`).then((data) => {
          this.getHashtags()
        })
      }
    },
    search: function() {
      if(this.onTab===2){
        console.log("해쉬a")
        this.getHashtags()       
      }
    },
  },
}
</script>

<style>

</style>