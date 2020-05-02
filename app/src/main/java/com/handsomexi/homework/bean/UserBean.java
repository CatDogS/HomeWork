package com.handsomexi.homework.bean;
public class UserBean {

    private int result_code;
    private String message;
    private DataBean data;

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 9
         * password : 测试
         * phonenum : 13071200389
         * name : 习大大
         * sex : 男
         * school : 武汉纺织大学
         * educational_level : 大三
         * grade : 上学期
         * vip_level : 0
         * register_time : 1541152957828
         * last_login_time : 1541152957828
         * vip_expiration_date : 0
         */

        private int id;
        private String password;
        private String phonenum;
        private String name;
        private String sex;
        private String school;
        private String educational_level;
        private String grade;
        private int vip_level;
        private String register_time;
        private String last_login_time;
        private String vip_expiration_date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhonenum() {
            return phonenum;
        }

        public void setPhonenum(String phonenum) {
            this.phonenum = phonenum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getEducational_level() {
            return educational_level;
        }

        public void setEducational_level(String educational_level) {
            this.educational_level = educational_level;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getVip_level() {
            return vip_level;
        }

        public void setVip_level(int vip_level) {
            this.vip_level = vip_level;
        }

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getVip_expiration_date() {
            return vip_expiration_date;
        }

        public void setVip_expiration_date(String vip_expiration_date) {
            this.vip_expiration_date = vip_expiration_date;
        }
    }
}