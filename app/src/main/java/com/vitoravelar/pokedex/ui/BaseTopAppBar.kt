package com.vitoravelar.pokedex.ui

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
            .height(56.dp)
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(colorResource(R.color.primary_color)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            },
            navigationIcon = {
                if (leftIcon != null) {
                    IconButton(onClick = { onLeftIconClick?.invoke() }) {
                        Icon(
                            imageVector = leftIcon,
                            contentDescription = "Left Icon",
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