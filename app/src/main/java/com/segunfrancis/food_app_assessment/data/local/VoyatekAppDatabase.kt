package com.segunfrancis.food_app_assessment.data.local

import com.segunfrancis.food_app_assessment.data.remote.Category
import com.segunfrancis.food_app_assessment.data.remote.Tag

/**
 * This class is used to simulate a local storage
 **/
class VoyatekAppDatabase {

    private var categories = listOf<Category>()
    private var tags = listOf<Tag>()

    fun setCategories(categories: List<Category>) {
        this.categories = categories
    }

    fun getCategories(): List<Category> {
        return categories
    }

    fun setTags(tags: List<Tag>) {
        this.tags = tags
    }

    fun getTags(): List<Tag> {
        return tags
    }
}
