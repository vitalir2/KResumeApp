package me.vitalir.kresume.server.dsl

import me.vitalir.kresume.server.model.Basics
import me.vitalir.kresume.server.model.Resume
import me.vitalir.kresume.server.model.WorkEntry

fun resume(block: ResumeBuilder.() -> Unit): Resume {
    val builder = ResumeBuilder()
    builder.block()
    return builder.build()
}

class ResumeBuilder {
    private var basicsBuilder: BasicsBuilder? = null
    private var workBuilders: MutableList<WorkEntryBuilder>? = null

    fun basics(block: BasicsBuilder.() -> Unit) {
        val builder = BasicsBuilder()
        builder.block()
        basicsBuilder = builder
    }

    fun work(block: WorkEntryBuilder.() -> Unit) {
        val builders = workBuilders ?: mutableListOf()
        val builder = WorkEntryBuilder()
        builder.block()
        builders.add(builder)
        workBuilders = builders
    }

    internal fun build(): Resume {
        return Resume(
            basics = basicsBuilder?.build(),
            work = workBuilders?.map { it.build() }
        )
    }
}

class BasicsBuilder {
    var name: String? = null
    var email: String? = null
    var phone: String? = null
    var summary: String? = null
    var location: String? = null
    var title: String? = null
    var url: String? = null

    internal fun build(): Basics {
        return Basics(
            name = name,
            email = email,
            phone = phone,
            summary = summary,
            location = location,
            title = title,
            url = url
        )
    }
}

class WorkEntryBuilder {
    var company: String? = null
    var position: String? = null
    var startDate: String? = null
    var endDate: String? = null
    var description: String? = null
    var highlights: List<String>? = null

    internal fun build(): WorkEntry {
        return WorkEntry(
            company = company,
            position = position,
            startDate = startDate,
            endDate = endDate,
            description = description,
            highlights = highlights
        )
    }
}
