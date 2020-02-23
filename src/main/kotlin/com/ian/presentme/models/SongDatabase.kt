package com.ian.presentme.models

/**
 * Map:
 * key = songId
 * values = [isActive, song]
 */
class SongDatabase(var db: MutableMap<Int, MutableList<Any>> = mutableMapOf()) {

    fun addSong(song: Song) {
        if (db.containsKey(song.id)) {
            db[song.id]?.set(1, song)
        } else {
            val value = mutableListOf(false, song)
            db[song.id] = value
        }
    }

    fun removeSong(id: Int) {
        db.remove(id)
    }

    fun getSong(id: Int): Song {
        return db[id]?.get(1) as Song
    }

    fun setSong(id: Int, song: Song) {
        db[id]?.let {
            it[1] = song
        }
    }

    fun getValue(id: Int): MutableList<Any>? {
        return db[id]
    }

    fun getValues(): List<Song> {
        val list = mutableListOf<Song>()
        db.values.forEach {
            list.add(it[1] as Song)
        }
        return list
    }
}