package com.mm.hamcompose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow

@Preview
@Composable
fun IconsPreview() {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .height(800.dp)
    ) {
        item {
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.AccountBox, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Create, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.MoreVert, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.MailOutline, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Menu, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
                Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
            FlowRow(Modifier.padding(bottom = 20.dp)) {
                Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.padding(end=10.dp)) 
            }
        }

    }
}
