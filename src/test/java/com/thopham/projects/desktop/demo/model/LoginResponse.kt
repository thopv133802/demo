/* 
Copyright (c) 2019 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class LoginResponse (
	val status : Boolean,
	val response : Response
)
data class Info (
        val id : Int,
        val username : String,
        val avatar : String,
        val short_name : String,
        val name : String,
        val birthday : String,
        val phone : Int,
        val email : String,
        val address : String,
        val is_aff : Int,
        val ref_code : String,
        val verify_phone : Int,
        val verify_email : Int,
        val num_notify : Int,
        val have_wallet : Boolean,
        val balance : Int,
        val adminres : Boolean,
        val role_editProduct : Boolean,
        val restaurant_list : List<Restaurant_list>,
        val is_merchant : Boolean,
        val restaurant_id : Int,
        val restaurant : Restaurant
)

data class Menu (

	val id : Int,
	val name : String
)

data class Response (

        val token : String,
        val info : Info,
        val date_now : Int,
        val date_end : Int,
        val token_card : String
)

data class Restaurant (

        val id : Int,
        val name : String,
        val logo : String,
        val background : String,
        val phone : Int,
        val topmenu_url : String,
        val website : String,
        val address : String,
        val district : String,
        val state : String,
        val lat : Double,
        val long : Double,
        val lowest_price : Int,
        val highest_price : Int,
        val capacity : Int,
        val introduction : String,
        val number_table : Int,
        val start_date : Int,
        val end_date : Int,
        val type_id : Int,
        val package_crm : Int,
        val sub_decs : String,
        val keywordswords : String,
        val intent : List<String>,
        val type : List<Int>,
        val utilities : List<Int>,
        val menu : List<Menu>,
        val is_res : Boolean
)

data class Restaurant_list (

        val id : Int,
        val name : String,
        val logo : String,
        val background : String,
        val phone : Int,
        val topmenu_url : String,
        val website : String,
        val address : String,
        val district : String,
        val state : String,
        val lat : Double,
        val long : Double,
        val lowest_price : Int,
        val highest_price : Int,
        val capacity : Int,
        val introduction : String,
        val number_table : Int,
        val start_date : Int,
        val end_date : Int,
        val type_id : Int,
        val package_crm : Int,
        val sub_decs : String,
        val keywordswords : String,
        val intent : List<String>,
        val type : List<Int>,
        val utilities : List<Int>,
        val menu : List<Menu>,
        val is_res : Boolean
)