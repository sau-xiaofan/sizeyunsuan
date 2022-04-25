<template>
  <div id="app">
    <el-container>
      <el-header  class="head">四则运算生成网站</el-header>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>

  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
  .head {
    text-align: center;
    line-height: 60px;
    font-size: 20px;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
  }
</style>
