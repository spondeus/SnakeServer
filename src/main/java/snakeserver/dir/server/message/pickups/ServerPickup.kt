package snakeserver.dir.server.message.pickups

import snakeserver.dir.util.Vector2
import java.util.Random

class ServerPickup(number: Int)
{
	val pickups= mutableListOf<Pickup>()
	val ids = mutableSetOf<Int>()

	fun removePickupById(id: Int){
		for(p in pickups)
			if(p.id == id)
				pickups.remove(p)
	}

	fun addPickup(num: Int){
		for(p in 0 until num){
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

	init
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