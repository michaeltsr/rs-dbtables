package com.michaeltsr.rs_dbtables.util

enum class ScriptVariableType(val id: Int, val keyChar: Char, val fullName: String)  {
    INTEGER(0, 'i', "integer"),
    BOOLEAN(1, '1', "boolean"),
    SEQ(6, 'A', "seq"),
    COLOUR(7, 'C', "colour"),
    COMPONENT(9, 'I', "component"),
    IDKIT(10, 'K', "idkit"),
    MIDI(11, 'M', "midi"),
    SYNTH(14, 'P', "synth"),
    STAT(17, 'S', "stat"),
    COORDGRID(22, 'c', "coordgrid"),
    GRAPHIC(23, 'd', "graphic"),
    FONTMETRICS(25, 'f', "fontmetrics"),
    ENUM(26, 'g', "enum"),
    JINGLE(28, 'j', "jingle"),
    LOC(30, 'l', "loc"),
    MODEL(31, 'm', "model"),
    NPC(32, 'n', "npc"),
    OBJ(33, 'o', "obj"),
    NAMEDOBJ(13, 'O', "namedobj"),
    STRING(36, 's', "string"),
    SPOTANIM(37, 't', "spotanim"),
    INV(39, 'v', "inv"),
    TEXTURE(40, 'x', "texture"),
    CHAR(42, 'z', "char"),
    MAPSCENEICON(55, '£', "mapsceneicon"),
    MAPELEMENT(59, 'µ', "mapelement"),
    HITMARK(62, '×', "hitmark"),
    STRUCT(73, 'J', "struct");

    companion object {
        private val idToTypeMap = mutableMapOf<Int, ScriptVariableType>()
        private val keyToTypeMap = mutableMapOf<Char, ScriptVariableType>()

        init {
            ScriptVariableType.values().forEach {
                if(it.id != -1) idToTypeMap[it.id] = it
                keyToTypeMap[it.keyChar] = it
            }
        }
    }

    fun idType(id: Int) : ScriptVariableType = idToTypeMap[id] ?: throw NullPointerException( "Invalid type id $id." )
    fun keyCharType(key: Char) : ScriptVariableType = requireNotNull(keyToTypeMap[key]) {"Invalid key character $key."}
}