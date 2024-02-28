package com.github.hoshinotented.minecraft.cft.network

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import com.github.hoshinotented.minecraft.cft.action.QuickMove
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.PlayPayloadContext
import net.neoforged.neoforge.server.ServerLifecycleHooks
import java.util.*

data class Packet(val uuid: UUID?) : CustomPacketPayload {
  companion object Handler : IPayloadHandler<Packet> {
    override val id = ResourceLocation(FundamentalTheorem.MODID, "packet")
    
    override fun encoder(payload: Packet, buf: FriendlyByteBuf) {
      if (payload.uuid == null) throw IllegalStateException("bad packet")
      buf.writeUUID(payload.uuid)
    }
    
    override fun decoder(buf: FriendlyByteBuf): Packet {
      val uuid = buf.readUUID()
      return Packet(uuid)
    }
    
    override fun handleServer(payload: Packet, context: PlayPayloadContext) {
      if (payload.uuid == null) throw IllegalStateException("bad packet")
      
      context.workHandler.submitAsync {
        val server = ServerLifecycleHooks.getCurrentServer()
          ?: throw IllegalStateException("Server didn't setup or here is client")
        val player = server.playerList.getPlayer(payload.uuid)
        if (player == null) {
          FundamentalTheorem.LOGGER.warn("Player ${payload.uuid} disappear.")
        } else {
          QuickMove.doQuickMove(player)
        }
      }
    }
  }
  
  override fun id(): ResourceLocation {
    return id
  }
  
  override fun write(buf: FriendlyByteBuf) {
    encoder(this, buf)
  }
}
