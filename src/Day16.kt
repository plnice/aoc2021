fun main() {
    fun part1(input: String): Int {
        val packet = input.toBits().parse().packet

        fun Packet.sumVersions(): Int = when (this) {
            is Literal -> version
            is Operator -> version + packets.sumOf { it.sumVersions() }
        }

        return packet.sumVersions()
    }

    fun part2(input: String): Long {
        val packet = input.toBits().parse().packet
        return packet.calculate()
    }

    val input = getMappedInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun getMappedInput(name: String) = readInput(name).first()

private fun String.toBits() = map {
    when (it) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> throw IllegalArgumentException()
    }
}.joinToString("")

private fun String.toChar() = padStart(4, '0').let {
    when (it) {
        "0000" -> '0'
        "0001" -> '1'
        "0010" -> '2'
        "0011" -> '3'
        "0100" -> '4'
        "0101" -> '5'
        "0110" -> '6'
        "0111" -> '7'
        "1000" -> '8'
        "1001" -> '9'
        "1010" -> 'A'
        "1011" -> 'B'
        "1100" -> 'C'
        "1101" -> 'D'
        "1110" -> 'E'
        "1111" -> 'F'
        else -> throw IllegalArgumentException()
    }
}

private sealed class Packet(open val version: Int, open val typeId: Int)
private data class Literal(override val version: Int, val value: Long) : Packet(version, 4)
private data class Operator(override val version: Int, override val typeId: Int, val packets: List<Packet>) :
    Packet(version, typeId)

private data class PacketWithSize(val packet: Packet, val size: Int)

private fun String.parse(): PacketWithSize {
    val typeId = Character.getNumericValue(this.substring(3, 6).toChar())
    return if (typeId == 4) this.toLiteral() else this.toOperator()
}

private fun String.toLiteral(): PacketWithSize {
    val version = Character.getNumericValue(substring(0, 3).toChar())

    var bitsConsumed = 6

    var current = toList().drop(6)
    var keepReading = true
    var result = ""
    while (keepReading) {
        val bits = current.take(5)
        current = current.drop(5)
        result += bits.drop(1).joinToString("")
        if (bits[0] == '0') keepReading = false
        bitsConsumed += 5
    }

    return PacketWithSize(Literal(version, result.toLong(2)), size = bitsConsumed)
}

private fun String.toOperator(): PacketWithSize {
    val version = Character.getNumericValue(substring(0, 3).toChar())
    val typeId = Character.getNumericValue(substring(3, 6).toChar())
    val lengthTypeId = this[6]

    var bitsConsumed = 7

    val subpackets = mutableListOf<Packet>()

    if (lengthTypeId == '0') {
        val subpacketsTotalLengthInBits = this.substring(bitsConsumed, bitsConsumed + 15).toLong(2)
        bitsConsumed += 15

        var consumed = 0
        while (consumed < subpacketsTotalLengthInBits) {
            val packetWithSize = this.substring(bitsConsumed + consumed).parse()
            subpackets.add(packetWithSize.packet)
            consumed += packetWithSize.size
        }
        bitsConsumed += consumed
    } else {
        val numberOfSubpackets = this.substring(bitsConsumed, bitsConsumed + 11).toLong(2)
        bitsConsumed += 11

        var consumed = 0
        while (consumed < numberOfSubpackets) {
            val packetWithSize = this.substring(bitsConsumed).parse()
            subpackets.add(packetWithSize.packet)
            bitsConsumed += packetWithSize.size
            consumed++
        }
    }

    return PacketWithSize(Operator(version, typeId, subpackets), bitsConsumed)
}

private fun Packet.calculate(): Long {
    return when (this) {
        is Literal -> value
        is Operator -> when (typeId) {
            0 -> packets.sumOf { it.calculate() }
            1 -> packets.fold(1) { acc, packet -> acc * packet.calculate() }
            2 -> packets.minOf { it.calculate() }
            3 -> packets.maxOf { it.calculate() }
            5 -> if (packets[0].calculate() > packets[1].calculate()) 1L else 0L
            6 -> if (packets[0].calculate() < packets[1].calculate()) 1L else 0L
            7 -> if (packets[0].calculate() == packets[1].calculate()) 1L else 0L
            else -> throw IllegalArgumentException()
        }
    }
}
