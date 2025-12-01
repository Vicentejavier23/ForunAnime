package com.animeforum.app.ui.components
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
@Composable fun RatingBar(rating:Int, onRatingChange:(Int)->Unit, maxRating:Int=5){ var anim by remember{ mutableStateOf(0) }; LaunchedEffect(rating){ for(i in 1..rating){ anim=i; kotlinx.coroutines.delay(50) } }; Row{ repeat(maxRating){ idx-> val i=idx+1; val s=animateFloatAsState(if(i<=anim)1.2f else 1f, spring(dampingRatio=Spring.DampingRatioMediumBouncy)).value; Icon(imageVector= if(i<=rating) Icons.Filled.Star else Icons.Outlined.StarOutline, contentDescription=null, tint= if(i<=rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, modifier=Modifier.clickable{ onRatingChange(i) }.graphicsLayer{ scaleX=s; scaleY=s }) } } }