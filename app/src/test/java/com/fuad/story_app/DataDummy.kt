package com.fuad.story_app

import com.fuad.story_app.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                photoUrl = "https://firebasestorage.googleapis.com/v0/b/bangkit-dashboard/o/production%2F2024-B2%2Fprofiles%2F6651e755-3fb6-49bd-b56d-58c9545a6a7e.jpg?alt=media&token=f72a6598-0a26-44b0-9c57-fdc0f538efd5",
                createdAt = "2024-12-18",
                name = "Fuad Hamidan",
                description = "Mobile Development",
                lon = 0.999,
                id = "asd0454sgrw3qw1wed",
                lat = 0.111
            )
            items.add(story)
        }
        return items
    }
}