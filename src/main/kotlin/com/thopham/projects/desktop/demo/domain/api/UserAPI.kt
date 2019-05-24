package com.thopham.projects.desktop.demo.domain.api

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.thopham.projects.desktop.demo.common.LoginInput
import com.thopham.projects.desktop.demo.common.LoginOutput
import feign.codec.Encoder
import feign.form.FormEncoder
import org.springframework.stereotype.Component
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.annotation.Bean
import com.thopham.projects.desktop.demo.domain.api.responseModels.LoginResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate


@Component
class UserAPI (val userClient: UserClient){
    companion object{
        private const val RESPONSE = "{\"status\":true,\"response\":{\"token\":\"NjE5MDUzODcyOGJlOGY3YjQyMGU4ZjJlZTZhN2ZiZjBlODIwYTQ1ZjFkZmM3Yjk1MjgyZDEwYjYwODdlMTFjMDlkNDkxODNhODczOTEwODAyMWUzMzEyZGFlZGJhN2Mx\",\"info\":{\"menuID\":935,\"username\":\"ducvm@vatgia.com\",\"avatar\":\"https:\\/\\/cdn.wesave.vn\\/upload\\/web\\/1\\/1\\/avatar\\/2018\\/04\\/09\\/01\\/04\\/152327904687.jpg\",\"short_name\":\"\",\"name\":\"Minh \\u0110\\u1ee9c\",\"birthday\":\"24\\/03\\/1995\",\"phone\":\"0904837295\",\"email\":\"ducvm@vatgia.com\",\"address\":null,\"is_aff\":1,\"ref_code\":\"WESAVE\",\"verify_phone\":1,\"verify_email\":1,\"num_notify\":0,\"have_wallet\":true,\"balance\":0,\"adminres\":true,\"role_editProduct\":true,\"restaurant_list\":[{\"menuID\":2647,\"name\":\"Nh\\u00e0 h\\u00e0ng Minh \\u0110\\u1ee9c\",\"logo\":\"https:\\/\\/tm.wesave.vn\\/cdn\\/restaurant\\/2647\\/web\\/2018\\/10\\/04\\/1538620717_6037.png\",\"background\":\"https:\\/\\/tm.wesave.vn\\/cdn\\/restaurant\\/2647\\/web\\/2018\\/10\\/04\\/1538620717_38469589_233527657300868_8989083001082085376_n.jpg\",\"phone\":\"0904837095\",\"topmenu_url\":\"http:\\/\\/wesave.vn\\/nha-hang-minh-duc?menuID=2647\",\"website\":\"http:\\/\\/duy.com\",\"address\":\"342 Kh\\u01b0\\u01a1ng \\u0110\\u00ecnh, Thanh Xu\\u00e2n, H\\u00e0 N\\u1ed9i\",\"district\":\"Qu\\u1eadn 1\",\"state\":\"H\\u1ed3 Ch\\u00ed Minh\",\"lat\":20.989506,\"long\":105.810374,\"lowest_price\":30000,\"highest_price\":490000,\"capacity\":0,\"introduction\":\"Chuy\\u00ean \\u0103n u\\u1ed1ng - \\u1ea9m th\\u1ef1c\",\"number_table\":150,\"start_date\":1520046013,\"end_date\":1598583613,\"sub_decs\":\" \",\"keywords\":\"thethao, bongda, store\",\"intent\":[],\"type\":[2],\"utilities\":[4,9,11],\"menu\":[{\"menuID\":3293,\"name\":\"Khai v\\u1ecb\"},{\"menuID\":3294,\"name\":\"Salad\"},{\"menuID\":3295,\"name\":\"G\\u00e0 kh\\u00f4ng \\u0111\\u1ed3i.\"},{\"menuID\":3296,\"name\":\"Chim c\\u00e2u\"},{\"menuID\":3297,\"name\":\"Tr\\u00e2u t\\u01b0\\u01a1i\"},{\"menuID\":3298,\"name\":\"B\\u00f2 t\\u01b0\\u01a1i\"},{\"menuID\":3299,\"name\":\"D\\u00ea t\\u01b0\\u01a1i\"},{\"menuID\":3300,\"name\":\"C\\u00e1 ninja\"},{\"menuID\":3301,\"name\":\"\\u1ed0c\"},{\"menuID\":3302,\"name\":\"\\u1ebech\"},{\"menuID\":3303,\"name\":\"M\\u1ef1c\"},{\"menuID\":3304,\"name\":\"M\\u00f3n \\u0111\\u1eb7c tr\\u01b0ng\"},{\"menuID\":3305,\"name\":\"C\\u01a1m - M\\u1ef3\"},{\"menuID\":3306,\"name\":\"C\\u01a1m - C\\u00e1c m\\u00f3n \\u0103n k\\u00e8m\"},{\"menuID\":3307,\"name\":\"Rau - \\u0110\\u1eadu\"},{\"menuID\":3308,\"name\":\"\\u0110\\u1ed3 u\\u1ed1ng\"},{\"menuID\":3501,\"name\":\"Ph\\u1edf\"}],\"is_res\":true},{\"menuID\":7925,\"name\":\"THEME TRILL\",\"logo\":\"https:\\/\\/tm.wesave.vn\\/cdn\\/restaurant\\/\\/web\\/2018\\/10\\/19\\/1539939329_6037.png\",\"background\":\"https:\\/\\/tm.wesave.vn\\/cdn\\/restaurant\\/7908\\/web\\/2018\\/04\\/06\\/1522980502_banner_header.jpg\",\"phone\":\"01232349867\",\"topmenu_url\":\"http:\\/\\/wesave.vn\\/theme-trill?menuID=7925\",\"website\":\"\",\"address\":\"102 Th\\u00e1i Th\\u1ecbnh, Trung Li\\u1ec7t, \\u0110\\u1ed1ng \\u0110a\",\"district\":\"Qu\\u1eadn Ba \\u0110\\u00ecnh\",\"state\":\"H\\u00e0 N\\u1ed9i\",\"lat\":0,\"long\":0,\"lowest_price\":0,\"highest_price\":0,\"capacity\":0,\"introduction\":\"Trill Group bao g\\u1ed3m t\\u1ed5 h\\u1ee3p Cafe, Nh\\u00e0 h\\u00e0ng, Gym & Pool v\\u00e0 T\\u1ed5 ch\\u1ee9c s\\u1ef1 ki\\u1ec7n. Trill trong ti\\u1ebfng Anh ngh\\u0129a l\\u00e0 \\u201ctrue\\u201d and \\u201creal\\u201d \\u2013 \\u201cth\\u1eadt\\u201d. M\\u1ecdi th\\u1ee9 \\u1edf Trill r\\u1ea5t th\\u1eadt, th\\u1eadt t\\u1eeb con ng\\u01b0\\u1eddi, d\\u1ecbch v\\u1ee5, \\u0111\\u1ed3 \\u0103n, \\u0111\\u1ed3 u\\u1ed1ng cho t\\u1edbi trang tr\\u00ed.\",\"number_table\":62,\"start_date\":1525143848,\"end_date\":1575083048,\"sub_decs\":\" \",\"keywords\":null,\"intent\":[],\"type\":[],\"utilities\":[],\"menu\":[{\"menuID\":3586,\"name\":\"c\\u1edbp\"}],\"is_res\":true},{\"menuID\":8790,\"name\":\"Test 3\",\"logo\":\"http:\\/\\/wesave.vn\\/themes\\/banner\\/avatar_df.png\",\"background\":\"http:\\/\\/cdn.wesave.vn\\/banner\\/cover_df.jpg\",\"phone\":\"01192832\",\"topmenu_url\":\"http:\\/\\/wesave.vn\\/test?menuID=8790\",\"website\":\"\",\"address\":\"\\u00e1dsdsasadsds\",\"district\":\"Qu\\u1eadn Ba \\u0110\\u00ecnh\",\"state\":\"H\\u00e0 N\\u1ed9i\",\"lat\":0,\"long\":0,\"lowest_price\":0,\"highest_price\":0,\"capacity\":0,\"introduction\":\"\",\"number_table\":24,\"start_date\":1534912359,\"end_date\":1572064359,\"sub_decs\":\" \",\"keywords\":null,\"intent\":[],\"type\":[],\"utilities\":[],\"menu\":[],\"is_res\":true},{\"menuID\":8921,\"name\":\"test 4\",\"logo\":\"http:\\/\\/wesave.vn\\/themes\\/banner\\/avatar_df.png\",\"background\":\"http:\\/\\/cdn.wesave.vn\\/banner\\/cover_df.jpg\",\"phone\":\"02932726212\",\"topmenu_url\":\"http:\\/\\/wesave.vn\\/test?menuID=8921\",\"website\":\"\",\"address\":\"h\\u00e0 n\\u1ed9i\",\"district\":\"Qu\\u1eadn Ba \\u0110\\u00ecnh\",\"state\":\"H\\u00e0 N\\u1ed9i\",\"lat\":0,\"long\":0,\"lowest_price\":0,\"highest_price\":0,\"capacity\":0,\"introduction\":\"\",\"number_table\":10,\"start_date\":7789,\"end_date\":1604113789,\"sub_decs\":\" \",\"keywords\":null,\"intent\":[],\"type\":[],\"utilities\":[],\"menu\":[],\"is_res\":true}],\"is_merchant\":true,\"restaurant_id\":2647,\"restaurant\":{\"menuID\":2647,\"name\":\"Nh\\u00e0 h\\u00e0ng Minh \\u0110\\u1ee9c\",\"logo\":\"https:\\/\\/tm.wesave.vn\\/cdn\\/restaurant\\/2647\\/web\\/2018\\/10\\/04\\/1538620717_6037.png\",\"background\":\"https:\\/\\/tm.wesave.vn\\/cdn\\/restaurant\\/2647\\/web\\/2018\\/10\\/04\\/1538620717_38469589_233527657300868_8989083001082085376_n.jpg\",\"phone\":\"0904837095\",\"topmenu_url\":\"http:\\/\\/wesave.vn\\/nha-hang-minh-duc?menuID=2647\",\"website\":\"http:\\/\\/duy.com\",\"address\":\"342 Kh\\u01b0\\u01a1ng \\u0110\\u00ecnh, Thanh Xu\\u00e2n, H\\u00e0 N\\u1ed9i\",\"district\":\"Qu\\u1eadn 1\",\"state\":\"H\\u1ed3 Ch\\u00ed Minh\",\"lat\":20.989506,\"long\":105.810374,\"lowest_price\":30000,\"highest_price\":490000,\"capacity\":0,\"introduction\":\"Chuy\\u00ean \\u0103n u\\u1ed1ng - \\u1ea9m th\\u1ef1c\",\"number_table\":150,\"start_date\":1520046013,\"end_date\":1598583613,\"sub_decs\":\" \",\"keywords\":\"thethao, bongda, store\",\"intent\":[],\"type\":[2],\"utilities\":[4,9,11],\"menu\":[{\"menuID\":3293,\"name\":\"Khai v\\u1ecb\"},{\"menuID\":3294,\"name\":\"Salad\"},{\"menuID\":3295,\"name\":\"G\\u00e0 kh\\u00f4ng \\u0111\\u1ed3i.\"},{\"menuID\":3296,\"name\":\"Chim c\\u00e2u\"},{\"menuID\":3297,\"name\":\"Tr\\u00e2u t\\u01b0\\u01a1i\"},{\"menuID\":3298,\"name\":\"B\\u00f2 t\\u01b0\\u01a1i\"},{\"menuID\":3299,\"name\":\"D\\u00ea t\\u01b0\\u01a1i\"},{\"menuID\":3300,\"name\":\"C\\u00e1 ninja\"},{\"menuID\":3301,\"name\":\"\\u1ed0c\"},{\"menuID\":3302,\"name\":\"\\u1ebech\"},{\"menuID\":3303,\"name\":\"M\\u1ef1c\"},{\"menuID\":3304,\"name\":\"M\\u00f3n \\u0111\\u1eb7c tr\\u01b0ng\"},{\"menuID\":3305,\"name\":\"C\\u01a1m - M\\u1ef3\"},{\"menuID\":3306,\"name\":\"C\\u01a1m - C\\u00e1c m\\u00f3n \\u0103n k\\u00e8m\"},{\"menuID\":3307,\"name\":\"Rau - \\u0110\\u1eadu\"},{\"menuID\":3308,\"name\":\"\\u0110\\u1ed3 u\\u1ed1ng\"},{\"menuID\":3501,\"name\":\"Ph\\u1edf\"}],\"is_res\":true}},\"date_now\":1556938184,\"date_end\":1598583613}}"
    }
    fun login(input: LoginInput): LoginOutput {
        val loginResponse= userClient.login(input)
        val isFail = !loginResponse.status
        if(isFail)
            throw Exception(loginResponse.mes)
        val userInform = loginResponse.response!!.info
        val restaurantInform = userInform.restaurant
        return LoginOutput(
                userId = userInform.id,
                username = userInform.username,
                restaurantId = restaurantInform.id,
                restaurantName = restaurantInform.name,
                token = loginResponse.response.token
        )
    }

}

@Component
class UserClient{
    private val restTemplate: RestTemplate = RestTemplate()
    @HystrixCommand(fallbackMethod = "loginTimeout")
    fun login(input: LoginInput): LoginResponse {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val map = LinkedMultiValueMap<String, String>()
        map.add("username", input.username)
        map.add("password", input.password)
        map.add("device_id", input.device_id)

        val request = HttpEntity(map, headers)
        val response = restTemplate.postForEntity(
                "https://api.wesave.vn/login",
                request,
                LoginResponse::class.java
        )
        return response.body ?: throw Exception("Login failed: " + response.statusCode.reasonPhrase)
    }
    private fun loginTimeout(input: LoginInput): LoginResponse {
        throw Exception("Kết nối quá hạn. Vui lòng kiểm tra lại mạng và thử lại")
    }
}

interface Client {
    class Configuration {
        @Bean
        fun feignFormEncoder(converters: ObjectFactory<HttpMessageConverters>): Encoder {
            return FormEncoder(SpringEncoder(converters))
        }
    }
}