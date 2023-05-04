package org.hyperskill.step.domain.model

import org.hyperskill.app.step.domain.model.Block

// TODO: make a stub of the block for concrete step (choice, code, etc.)

fun Block.Companion.stub(
    name: String = "",
    text: String = "",
    options: Block.Options = Block.Options()
): Block =
    Block(
        name = name,
        text = text,
        options = options
    )
