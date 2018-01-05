package au.edu.une.grewsum.grewsum

import java.util.Date

data class Paddock(
        var name: String,
        var size: Double
) {
}

data class GrazingPeriod(
        var paddockArea: Double?,
        var startMass: Double?,
        var residualMass: Double?,
        var dsePerHead: Double?,
        var stockUnits: Double?
) {

    fun grazingPeriod():Double? {
        val deltaMass = startMass?.let { sm ->
            residualMass?.let { rm ->
                sm - rm
            }
        }

        val consumption = dsePerHead?.let { dph ->
            stockUnits?.let { su ->
                dph * su
            }
        }

        val consumptionDensity = consumption?.let { c ->
            paddockArea?.let { s ->
                c/s
            }
        }

        val gp = deltaMass?.let { dm ->
            consumptionDensity?.let { cd ->
                dm / cd
            }
        }

        return gp
    }
}

const val oneDay = 1000 * 60 * 60 * 24

data class FeedBudget(
        var farmArea: Double?,
        var start: Date?,
        var end: Date?,
        var startHerbageMass: Double?,
        var endHerbageMass: Double?,
        var growthRate: Double?,
        var dsePerHead: Double?
) {

    fun periodLength():Long? {
        val p = end?.let { e ->
            start?.let { s ->
                (e.time - s.time) / oneDay
            }
        }

        return p
    }

    fun availableFeed():Double? {
        val delta = startHerbageMass?.let { s ->
            endHerbageMass?.let { e ->
                s - e
            }
        }

        return delta?.let { del ->
            periodLength()?.let { p ->
                growthRate?.let { gr ->
                    del / p + gr
                }
            }
        }
    }

    fun stockDensity():Double? {
        return availableFeed()?.let { af ->
            dsePerHead?.let { dph ->
                af / dph
            }
        }
    }

    fun stockUnits():Double? {
        return stockDensity()?.let { sd ->
            farmArea?.let { fa ->
                sd * fa
            }
        }
    }


}
