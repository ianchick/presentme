package com.ian.presentme.models

class SetDatabase(var db: MutableMap<Int, MutableList<Any>> = mutableMapOf()) {
    fun setActiveSet(id: Int) {
        db.values.forEach {
            if (it[0] == true) {
                it[0] = false
            }
        }
        db[id]?.let {
            it[0] = true
        }
    }

    fun getActiveSet(): SetList? {
        db.values.forEach {
            if (it[0] == true) {
                return it[1] as SetList
            }
        }
        return null
    }

    fun setSetList(id: Int, setList: SetList) {
        if (db.containsKey(id)) {
            db[id]?.set(1, setList)
        } else {
            val value = mutableListOf(false, setList)
            db[id] = value
        }
    }

    fun getValues(): List<SetList> {
        val list = mutableListOf<SetList>()
        db.values.forEach {
            list.add(it[1] as SetList)
        }
        return list
    }

    fun removeSong(songId: Int) {
        val list = mutableListOf<Int>()
        getValues().forEach { set ->
            if (set.songIds.contains(songId)) {
                list.add(set.id)
            }
        }
        list.forEach { id ->
            db[id]?.let {
                (it[1] as SetList).songIds.remove(songId)
            }
        }
    }
}