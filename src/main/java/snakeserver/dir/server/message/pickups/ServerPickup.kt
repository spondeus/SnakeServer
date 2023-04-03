package snakeserver.dir.server.message.pickups

import snakeserver.dir.util.Vector2
import java.util.Random

class ServerPickup()
{
	val pickups= mutableListOf<Pickup>()
	val ids = mutableSetOf<Int>()

	fun removePickupById(id: Int){
		ids.remove(id)
		var pickup: Pickup? = null
		for(p in pickups)
			if(p.pickUpId == id)
				pickup = p
		pickups.remove(pickup)
	}

	fun reset(){
		pickups.removeAll(pickups)
	}

	fun addPickup(pickup: Pickup){
		pickups.add(pickup)
	}

	fun newPickup(): Pickup {
		val random = Random().nextInt(1,7)
		while(true){
			val id = Random().nextInt(1,100)
			if(!ids.contains(id)){
				ids.add(id)
				return Pickup(
						intToType(random),
						id,
						Vector2(
							Random().nextInt(100, 1100).toFloat(),
							Random().nextInt(100, 500).toFloat()
						))
			}
		}
	}

	fun intToType(num: Int): Type?{
		return when (num){
			1 -> Type.FOOD
			2 -> Type.DRINK
			3 -> Type.POISON
			4 -> Type.WEB
			5 -> Type.ICE
			6 -> Type.GHOST
			else -> null
		}
	}

	constructor(number: Int) : this()
	{
		for (i in 0 until number){
			val random = Random().nextInt(1,6)

			while(true){
				val id = Random().nextInt(1,100)
				if(!ids.contains(id)){
					ids.add(id)
					pickups.add(
							Pickup(
								intToType(random),
								id,
								Vector2(
									Random().nextInt(100, 1100).toFloat(),
									Random().nextInt(100, 700).toFloat(),
							)))
					break
				}
			}
		}
	}
}