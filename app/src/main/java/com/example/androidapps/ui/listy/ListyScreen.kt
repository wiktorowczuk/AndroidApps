package com.example.androidapps.ui.listy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class Item(
    val text: String,
    val isSelected: Boolean = false
)


@Composable
fun DualListScreen() {

    val listA = remember {
        mutableStateListOf(
            Item("A"), Item("B"), Item("C"), Item("D"), Item("E"),
            Item("F"), Item("G"), Item("H"), Item("I"), Item("J"), Item("K")
        )
    }

    val listB = remember { mutableStateListOf<Item>() }

    fun moveSelected(from: SnapshotStateList<Item>, to: SnapshotStateList<Item>) {
        val selected = from.filter { it.isSelected }

        to.addAll(selected.map { it.copy(isSelected = false) })
        from.removeAll(selected)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {

            //pierwsza lista
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {

                Text(
                    text = "Lista pierwsza",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(24.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(listA) { index, item ->
                        ItemRow(
                            item = item,
                            onClick = {
                                listA[index] = item.copy(isSelected = !item.isSelected)
                            }
                        )
                    }
                }
            }

            //druga lista
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {

                Text(
                    text = "Lista druga",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(24.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(listB) { index, item ->
                        ItemRow(
                            item = item,
                            onClick = {
                                listB[index] = item.copy(isSelected = !item.isSelected)
                            }
                        )
                    }
                }
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = { moveSelected(listA, listB) },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp).padding(bottom = 72.dp)
            ) {
                Text(
                    "Przenieś zaznaczone elementy na drugą listę",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { moveSelected(listB, listA) },
                modifier = Modifier.weight(1f).heightIn(min = 48.dp).padding(bottom = 72.dp)
            ) {
                Text(
                    "Przenieś zaznaczone elementy na pierwszą listę",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
private fun ItemRow(
    item: Item,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().background(
                if (item.isSelected) Color(0xFFD6EAF8)
                else Color.Transparent
        ).clickable { onClick() }.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = item.text,
            fontSize = 16.sp
        )
    }
}