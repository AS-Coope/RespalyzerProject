package com.example.respalyzerproject

interface onItemClickListener {
    // necessary for determining which item in the recycler view is clicked so only its audio plays
    fun onItemClickListener(position: Int) // gets the position of the item from the recycler view
}