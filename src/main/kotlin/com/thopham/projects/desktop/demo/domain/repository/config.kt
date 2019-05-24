package com.thopham.projects.desktop.demo.domain.repository

import com.zaxxer.hikari.util.DriverDataSource
import org.hibernate.dialect.Dialect
import org.hibernate.dialect.identity.IdentityColumnSupport
import org.hibernate.dialect.identity.IdentityColumnSupportImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.sql.Types
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
open class Config{
    @Autowired
    lateinit var environment: Environment

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()

        dataSource.setDriverClassName(environment.getProperty("driverClassName") ?: "org.sqlite.JDBC")
        dataSource.url = environment.getProperty("url")
        dataSource.username = environment.getProperty("user")
        dataSource.password = environment.getProperty("password")

        return dataSource
    }
    @PostConstruct
    fun seedDatabase(){
        //TODO: Seed database
    }
}

class SQLiteDialect: Dialect() {
    init {
        registerColumnType(Types.BIT, "integer")
        registerColumnType(Types.TINYINT, "tinyint")
        registerColumnType(Types.SMALLINT, "smallint")
        registerColumnType(Types.INTEGER, "integer")
    }

    override fun getAddColumnString(): String {
        return ""
    }

    override fun getAddColumnSuffixString(): String {
        return ""
    }
    override fun getIdentityColumnSupport(): IdentityColumnSupport {
        return SQLiteIdentityColumnSupport()
    }

    override fun hasAlterTable(): Boolean {
        return false
    }

    override fun dropConstraints(): Boolean {
        return false
    }

    override fun getDropForeignKeyString(): String {
        return ""
    }

    override fun getAddForeignKeyConstraintString(constraintName: String?, foreignKey: Array<out String>?, referencedTable: String?, primaryKey: Array<out String>?, referencesPrimaryKey: Boolean): String {
        return ""
    }

    override fun getAddPrimaryKeyConstraintString(constraintName: String?): String {
        return ""
    }
}

class SQLiteIdentityColumnSupport : IdentityColumnSupportImpl() {
    override fun supportsIdentityColumns(): Boolean {
        return true
    }

    override fun getIdentitySelectString(table: String, column: String, type: Int): String {
        return "select last_insert_rowid()"
    }

    override fun getIdentityColumnString(type: Int): String {
        return "integer"
    }

}

