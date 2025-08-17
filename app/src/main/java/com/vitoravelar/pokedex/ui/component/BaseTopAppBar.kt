package com.vitoravelar.pokedex.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitoravelar.pokedex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    title: String,
    leftIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    onLeftIconClick: (() -> Unit)? = null,
    rightIcon: ImageVector? = null,
    onRightIconClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .height(100.dp)
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 22.sp
                )
            },
            navigationIcon = {
                if (leftIcon != null) {
                    IconButton(onClick = { onLeftIconClick?.invoke() }) {
                        Icon(
                            imageVector = leftIcon,
                            contentDescription = "Left Icon",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                }
            },
            actions = {
                if (rightIcon != null) {
                    IconButton(onClick = { onRightIconClick?.invoke() }) {
                        Icon(
                            imageVector = rightIcon,
                            contentDescription = "Right Icon",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
@Preview(device = "spec:width=720px,height=1280px,dpi=320,orientation=portrait")
fun PreviewGenericTopAppBar() {
    BaseTopAppBar("Payment Type", onLeftIconClick = { })
}