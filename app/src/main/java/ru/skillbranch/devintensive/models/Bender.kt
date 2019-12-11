package ru.skillbranch.devintensive.models

import android.text.TextUtils.replace

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion() : String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question

    }

    fun listenAnswer(answer: String) : Pair<String, Triple<Int, Int, Int>> {
        val errorValidation: String

        // in IDLE mode return without changing status
        if(question == Question.IDLE) {
            return "${question.question}" to status.color
        }

        // validation without changing status
        errorValidation = validationOfAnswer(answer)
        if(errorValidation != "") {
            return "$errorValidation\n${question.question}" to status.color
        }

        // check answer with changing status
        if (question.answers.contains(answer)){
            question = question.nextQuestion()
            return "Отлично - ты справился\n${question.question}" to status.color
        } else {
            status = status.nextStatus()
            if(status == Status.NORMAL) {
                question = Question.NAME
                return "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            } else {
                return "Это неправильный ответ\n${question.question}" to status.color
            }
        }

    }

    fun validationOfAnswer(answer: String): String {
        var text: String = ""
        val answerWoSpace: String = answer.trim()

        if(answerWoSpace.isNotEmpty()) {
            when (question) {
                Question.NAME -> {
                    if (!(answerWoSpace[0] in 'A'..'Z' || answerWoSpace[0] in 'А'..'Я'))
                        text = "Имя должно начинаться с заглавной буквы"
                }
                Question.PROFESSION -> {
                    if (!(answerWoSpace[0] in 'a'..'z' || answerWoSpace[0] in 'а'..'я'))
                        text = "Профессия должна начинаться со строчной буквы"
                }
                Question.MATERIAL -> {
                    if (answerWoSpace.contains("\\d+".toRegex()))
                        text = "Материал не должен содержать цифр"
                }
                Question.BDAY -> {
                    if (answerWoSpace.contains("\\D+".toRegex()))
                        text = "Год моего рождения должен содержать только цифры"
                }
                Question.SERIAL -> {
                    if (answerWoSpace.contains("\\D+".toRegex()) || answerWoSpace.length != 7)
                        text = "Серийный номер содержит только цифры, и их 7"
                }
                else -> text = "" // Question.IDLE -> ignoring validation
            }
        }

        return text
    }


    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if(this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            }else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Bender","Бендер")) {
            override fun nextQuestion() = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("bender","сгибальщик")) {
            override fun nextQuestion() = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл","дерево","iron","wood","metal")) {
            override fun nextQuestion() = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion() = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion() = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion() = IDLE
        };

        abstract fun nextQuestion(): Question
    }
}