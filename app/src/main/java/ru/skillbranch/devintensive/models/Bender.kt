package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion():String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question

    }

    fun listenAnswer(answer: String) : Pair<String, Triple<Int, Int, Int>> {
        return if (question.answers.contains(answer)){
            question = question.nextQuestion()
            "Отлично! Это правильный ответ!\n${question.question}" to status.color
        }else{
            status = status.nextStatus()
            "Это неправильный ответ!\n${question.question}" to status.color
        }

    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 255, 0));

        fun nextStatus(): Status {
            return if(this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            }else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("bender","бендер")) {
            override fun nextQuestion() = PROFESSION

        },
        PROFESSION("Назови мою профессию.", listOf("bender","сгибальщик")) {
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
        IDLE("На этом всё, пока вопросов нет.", listOf()) {
            override fun nextQuestion() = IDLE

        };

        abstract fun nextQuestion(): Question
    }
}