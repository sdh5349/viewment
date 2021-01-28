<template>
  <div>{{aa}}</div>
</template>

<script>
export default {
  data() {
    return {
      aa : ""
    }
  },
  props: {
    search : {
      type: String,
    }
  },
  methods: {
    goSelect() {
      this.aa = this.search
    }
  },
  created() {
    this.goSelect()
  },
}
</script>

<style>

</style>