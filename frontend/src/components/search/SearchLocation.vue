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
    select : {
      type: Object
    }
  },
  methods: {
    goSelect() {
      this.aa = this.select
    }
  },
  created() {
    this.goSelect()
  },
}
</script>

<style>

</style>