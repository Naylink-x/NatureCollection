package fr.naylink.naturecollection

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.naylink.naturecollection.PlantRepository.Singleton.databaseRef
import fr.naylink.naturecollection.PlantRepository.Singleton.plantList

class PlantRepository {

    object Singleton {
        // se connecter à la reference "plants"
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // créer une liste qui va contenir nos plantes
        val plantList = arrayListOf<PlantModel>()
    }

    fun updateData(callback: () -> Unit) {
        // absorber les données depuis la databaseRef -> liste de plantes
        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                // retirer les anciennes
                plantList.clear()
                // recolter la liste
                for (ds in p0.children) {
                    // construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)

                    // verifier que la plante n'est pas null
                    if (plant != null) {
                        // ajouter la plante à notre liste
                        plantList.add(plant)
                    }
                }
                // actionner le callback
                callback()
            }

            override fun onCancelled(p0: DatabaseError) {}

        })
    }

    // mettre à jour un objet plante en base de données
    fun updatePlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

}