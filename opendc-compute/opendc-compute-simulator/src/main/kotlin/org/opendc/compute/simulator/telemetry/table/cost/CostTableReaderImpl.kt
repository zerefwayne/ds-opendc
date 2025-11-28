/*
 * Copyright (c) 2025 AtLarge Research
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.opendc.compute.simulator.telemetry.table.cost

import org.opendc.compute.api.TaskState
import org.opendc.compute.simulator.host.SimHost
import org.opendc.compute.simulator.service.ComputeService
import org.opendc.compute.simulator.service.ServiceTask
import org.opendc.compute.simulator.telemetry.table.host.HostInfo
import java.time.Duration
import java.time.Instant

/**
 * An aggregator for task metrics before they are reported.
 */
public class CostTableReaderImpl(
    private val service: ComputeService,
    private val startTime: Duration = Duration.ofMillis(0),
) : CostTableReader {
    override fun copy(): CostTableReader {
        val newTaskTable =
            CostTableReaderImpl(
                service
            )
        newTaskTable.setValues(this)

        return newTaskTable
    }

    override fun setValues(table: CostTableReader) {
        _timestamp = table.timestamp
        _timestampAbsolute = table.timestampAbsolute
    }

    private var _timestamp = Instant.MIN
    override val timestamp: Instant
        get() = _timestamp

    private var _timestampAbsolute = Instant.MIN
    override val timestampAbsolute: Instant
        get() = _timestampAbsolute

    private var _money = 0.0
    override val money: Double
        get() = _money

    /**
     * Record the next cycle.
     */
    override fun record(now: Instant) {
        _timestamp = now
        _timestampAbsolute = now + startTime
        _money = money
    }

    /**
     * Finish the aggregation for this cycle.
     */
    override fun reset() {
        _money = 0.0
    }
}
