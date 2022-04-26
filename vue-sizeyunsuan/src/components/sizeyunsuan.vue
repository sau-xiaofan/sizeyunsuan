<template>
  <div>
    <el-form :inline="true" :model="form" :rules="rules" ref="form" class="demo-form-inline">
      <el-form-item label="题目数量" prop="num">
        <el-input-number v-model="form.num" :min="1" label="题目数量" style="width: 150px"></el-input-number>
      </el-form-item>
      <el-form-item label="运算符" prop="types">
        <el-checkbox-group v-model="form.types" @change="typesChange">
          <el-checkbox-button label="+" name="1"></el-checkbox-button>
          <el-checkbox-button label="-" name="2"></el-checkbox-button>
          <el-checkbox-button label="*" name="3"></el-checkbox-button>
          <el-checkbox-button label="/" name="4"></el-checkbox-button>
          <el-checkbox-button label="括号" name="5"></el-checkbox-button>
          <el-checkbox-button label="小数" name="6"></el-checkbox-button>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="小数位数">
        <el-input-number v-model="form.doubleNum" :disabled="doubleNumDisabled" :min="1" style="width: 150px"></el-input-number>
      </el-form-item>
      <el-form-item label="范围" prop="range" >
        <el-select v-model="form.range" clearable placeholder="请选择" style="width: 150px">
          <el-option
            v-for="item in form.options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('form')">生成</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
      </el-form-item>
    </el-form>
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-download"
      @click="downloadTemplate"
    >导出</el-button
    >
    <el-table
      class="my-one-row"
      :data="tableData"
    >
      <el-table-column
        prop="id"
        label="序号"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="formula"
        label="四则运算式"
        style="width: 80%"
      ></el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        v-model:currentPage="pagination.currentPage"
        layout="total,sizes,prev,pager,next,jumper"
        :total="pagination.totalResult"
        :page-sizes="[10, 20, 30, 40, 50, 60]"
        v-model:page-size="pagination.showCount"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      ></el-pagination>
    </div>
  </div>
</template>
<script>
export default {
  name: "sizeyunsuan",
  data() {
    return {
      totalData: [],
      doubleNumDisabled: true,
      pagination: {
        currentPage: 1,
        totalResult: 0,
        showCount: 10
      },
      tableData: [],
      form: {
        doubleNum: '',
        num: '',
        types: [],
        range: '',
        options: [{
          value: '10',
          label: '十以内'
        }, {
          value: '100',
          label: '百以内'
        }, {
          value: '1000',
          label: '千以内'
        }, {
          value: '10000',
          label: '万以内'
        }, {
          value: '100000',
          label: '十万以内'
        }, {
          value: '1000000',
          label: '百万以内'
        }, {
          value: '10000000',
          label: '千万以内'
        }],
      },
      rules: {
        num: [
          { required: true, message: '请输入题目数量', trigger: 'blur' },
        ],
        types: [
          { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
        ],
        range: [
          { required: true, message: '请选择范围', trigger: 'change' }
        ]
      }
    };
  },
  methods: {
    getList() {
      var param;
      if(this.doubleNumDisabled) {
        param = {
          num: this.form.num,
          types: this.form.types,
          range: this.form.range,
          currentPage: this.pagination.currentPage,
          showCount: this.pagination.showCount
        };
      } else {
        param = {
          doubleNum: this.form.doubleNum,
          num: this.form.num,
          types: this.form.types,
          range: this.form.range,
          currentPage: this.pagination.currentPage,
          showCount: this.pagination.showCount
        };
      }
      this.$axios
        .post('/formula/listFormula', param)
        .then((res) => {
          if (res.data.result == 'success') {
            this.pagination.totalResult = res.data.page.totalResult;
            this.totalData = res.data.varList
            let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
            let end = this.pagination.currentPage * this.pagination.showCount
            this.tableData = this.totalData.slice(begin, end);
           } else {
            this.$message.error(res.data.message);
          }
        })
        .catch((error) => {

        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.getList();
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    handleSizeChange(val) {
      this.pagination.showCount = val;    //动态改变
      let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
      let end = this.pagination.currentPage * this.pagination.showCount
      this.tableData = this.totalData.slice(begin, end);
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val;    //动态改变
      let begin = (this.pagination.currentPage - 1) * this.pagination.showCount
      let end = this.pagination.currentPage * this.pagination.showCount
      this.tableData = this.totalData.slice(begin, end);
    },
    typesChange(val) {
      var res = false;
      for (let i = 0; i < val.length; i++) {
        if(val[i] == '小数') {
          res = true;
          break;
        }
      }
      if(res) {
        this.doubleNumDisabled = false;
      } else {
        this.doubleNumDisabled = true;
      }
    },
    downloadTemplate() {
      if(this.totalData.length == 0) {
        this.$message.error("当前没有生成过四则运算式，请生成！");
        return false;
      }
      const param = {
        varList: this.totalData
      };
      this.$axios
        .post('/formula/excel', param, {responseType: 'blob'})
        .then(function (response) {
          const url = window.URL.createObjectURL(new Blob([response.data]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', '四则运算式.xls')
          document.body.appendChild(link)
          link.click()
        })
        .catch((error) => {

        });
    }
  },
  created() {

  }
};
</script>
