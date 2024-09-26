package test.henry_meds.data

import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

typealias Availability = Set<OpenEndRange<LocalTime>>
typealias Schedule = Map<LocalDate, Availability>

fun Schedule.plus(
	timeRange: OpenEndRange<LocalDateTime>,
	timeZone: TimeZone = TimeZone.currentSystemDefault()
): Schedule =
	if (timeRange.start.date == timeRange.endExclusive.date) {
		plus(timeRange.start.date, timeRange.start.time..<timeRange.endExclusive.time)
	} else {
		// TODO: Verify end of day logic (maybe update Availability.plus check?)
		var nextDay = timeRange.start.date.atStartOfDayIn(timeZone) + 1.days

		var newSchedule = plus(timeRange.start.date, timeRange.start.time..<nextDay.toLocalDateTime(timeZone).time)

		while (nextDay.toLocalDateTime(timeZone).date < timeRange.endExclusive.date) {
			val dayStart = nextDay.toLocalDateTime(timeZone)

			nextDay += 1.days
			val dayEnd = nextDay.toLocalDateTime(timeZone)

			newSchedule = newSchedule.plus(dayStart.date, dayStart.time..<dayEnd.time)
		}

		newSchedule.plus(
			timeRange.endExclusive.date,
			nextDay.toLocalDateTime(timeZone).time..<timeRange.endExclusive.time
		)
	}

fun Schedule.plus(
	date: LocalDate,
	timeRange: OpenEndRange<LocalTime>
): Schedule =
	this + (date to getOrElse(date, ::emptySet) + timeRange)

operator fun Availability.plus(timeRange: OpenEndRange<LocalTime>): Availability {
	val overlappingRanges = filter {
		it.start in timeRange || it.endExclusive in timeRange
	}

	return if (overlappingRanges.isEmpty()) {
		plusElement(timeRange)
	} else {
		val mergedRange = (overlappingRanges + timeRange).reduce { acc, range ->
			val start = minOf(acc.start, range.start)
			val end = maxOf(acc.endExclusive, range.endExclusive)

			start..<end
		}

		(this - overlappingRanges).plusElement(mergedRange)
	}
}

private val slotDuration = 15.minutes

// TODO: Filter slots within 24 hours of now
// TODO: Handle case where a slot overlaps two days (on either end)
fun Availability.toSlots(
	date: LocalDate,
	timeZone: TimeZone = TimeZone.currentSystemDefault()
) = flatMap {
	buildSet {
		var instant = date.atTime(it.start).toInstant(timeZone)
		val end = date.atTime(it.endExclusive).toInstant(timeZone)

		while (instant < end) {
			val slotStart = instant.toLocalDateTime(timeZone).time

			instant += slotDuration
			val slotEnd = instant.toLocalDateTime(timeZone).time

			add(slotStart..<slotEnd)
		}
	}
}
