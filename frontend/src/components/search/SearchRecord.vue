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
            v-for="(History,i) in Historys"
            :key="i"
          >
            <v-list-item-avatar>
              <v-icon>
                {{History.HistoryIcon}}
              </v-icon>
            </v-list-item-avatar>    
            <v-list-item-content>
              <v-list-item-title >{{History.HistoryTitle}}</v-list-item-title>
              <v-list-item-title >{{History.HistoryContent}}</v-list-item-title>
              <v-list-item-title >{{i}}</v-list-item-title>
            </v-list-item-content>

            <v-list-item-icon>
              <v-icon
                @click="removeHistory()"
              >
                mdi-close
              </v-icon>
            </v-list-item-icon>
          </v-list-item>
        </v-list>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
export default {
  data() {
    return {
      Historys: {},
      // removedHistory: {},
    }
  },
  props:{
    search : {
      type: String,
    },
    onTab : {
      type: Number,
    }
  },
  methods: {
    getSearchedlog() {
      var str = localStorage.getItem("Historys");
      console.log(str)
      var obj = {};
      console.log(obj)
      try {
        obj = JSON.parse(str);
        console.log(obj)
      } catch {
        obj = {};
        console.log(obj)
      }
      if(!obj){
        obj = {};
        console.log(obj)
        obj["Historys"] = [];
        console.log(obj)
      }
      this.Historys = obj["Historys"]
      console.log(this.Historys)
      console.log(typeof(this.Historys))
    },
    removeHistory() {
      // var getLocalStorage = localStorage.getItem("Historys")
      console.log(this.Historys)
      console.log(typeof(this.Historys))
      // const delHistorys = this.Historys
      this.Historys.splice(History, 1)
      // this.obj = delete delHistorys.History
      console.log(this.Historys)
      console.log(typeof(this.Historys))
      
      // getLocalStorage.Historys.splice(History,1)
      localStorage.setItem("Historys", (JSON.stringify(this.Historys)))
      console.log(JSON.parse(localStorage.getItem("Historys")))
      
      // this.getSearchedlog(this.Historys)
    }
  },
  created() {
    this.getSearchedlog()
  },
  watch: {
    search: function() {
      console.log("기록a")
      if(this.onTab===0){
        this.getSearchedlog()
        
      }
    },
    onTab: function() {
      console.log("기록b")
      if(this.onTab===0){
        this.getSearchedlog()
      }
    }
  },
}
</script>

<style>

</style>