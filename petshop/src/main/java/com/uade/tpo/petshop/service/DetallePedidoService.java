package com.uade.tpo.petshop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.uade.tpo.petshop.entity.DetallePedido;
import com.uade.tpo.petshop.entity.Pedido;
import com.uade.tpo.petshop.entity.Producto;
import com.uade.tpo.petshop.entity.Usuario;
import com.uade.tpo.petshop.entity.dtos.DetallePedidoDTO;
import com.uade.tpo.petshop.entity.dtos.ProductoDTO;
import com.uade.tpo.petshop.entity.exceptions.InvalidDataException;
import com.uade.tpo.petshop.entity.exceptions.MissingPedidoException;
import com.uade.tpo.petshop.entity.exceptions.MissingProductoException;
import com.uade.tpo.petshop.entity.exceptions.MissingStockException;
import com.uade.tpo.petshop.entity.exceptions.NotFoundException;
import com.uade.tpo.petshop.entity.exceptions.PedidoCanceladoException;
import com.uade.tpo.petshop.entity.exceptions.UnauthorizedException;
import com.uade.tpo.petshop.repositories.interfaces.IDetallePedidoRepository;
import com.uade.tpo.petshop.service.interfaces.IDetallePedidoService;
import com.uade.tpo.petshop.service.interfaces.IPedidoService;
import com.uade.tpo.petshop.service.interfaces.IProductoService;

import jakarta.transaction.Transactional;

@Service
public class DetallePedidoService implements IDetallePedidoService {

    @Autowired
    private IDetallePedidoRepository detallePedidoRepository;

    @Autowired
    private IPedidoService pedidoService;

    @Autowired
    private IProductoService productoService;

    public DetallePedidoService(IDetallePedidoRepository detallePedidoRepository, IPedidoService pedidoService, IProductoService productoService){
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    public DetallePedidoService(){
        
    }

    @Override
    public Page<DetallePedido> findAll(PageRequest pageRequest) {
        return detallePedidoRepository.findAll(pageRequest);
    }

    @Override
    public Optional<DetallePedido> findById(Long id) {
        return detallePedidoRepository.findById(id);
    }

    @Override
    public Page<DetallePedido> findByUsuarioId(Long clienteId, PageRequest pageRequest){
        return detallePedidoRepository.findByPedidoClienteId(clienteId, pageRequest);
    }

    @Override
    @Transactional
    public DetallePedido save(DetallePedidoDTO detallePedidoDTO) throws MissingPedidoException, MissingProductoException, MissingStockException, PedidoCanceladoException {
        Pedido pedido = pedidoService.getPedidoById(detallePedidoDTO.getPedidoId()).orElseThrow(MissingPedidoException::new);
        
        //Verifica que el pedido no haya sido cancelado
        // if (pedido.getEstado() == EstadoEnum.CANCELADO){
        //     throw new PedidoCanceladoException();
        // }

        Producto producto = productoService.getProductoById(detallePedidoDTO.getProductoId()).orElseThrow(MissingProductoException::new);   

        //Actualiza el Stock y si no es suficiente, tira excepcion
        if (detallePedidoDTO.getCantidad() > producto.getStock()){
            throw new MissingStockException();
        } else {
            productoService.updateStock(producto.getId(), new ProductoDTO(producto.getStock() - detallePedidoDTO.getCantidad()));
        }

        DetallePedido detalle = new DetallePedido(pedido, producto, detallePedidoDTO.getCantidad());

        detalle = detallePedidoRepository.save(detalle);
        pedidoService.agregarDetalleAPedido(detalle);
        return detalle;
    }

    @Override
    @Transactional
    public DetallePedido update(Long id, DetallePedidoDTO detallePedidoDTO, Usuario usuario) //Le paso la sesion del usuario logueado junto con el id del detalle a modificar y el/los campo/s
            throws UnauthorizedException, InvalidDataException, NotFoundException {
        DetallePedido detallePedido= detallePedidoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    
        boolean pertenece=detallePedido.getPedido().getCliente().getId().equals(usuario.getId());
        if (!pertenece){
            throw new UnauthorizedException();
        }

        if (detallePedidoDTO.getCantidad()<=0){
            throw new InvalidDataException();
        }

        int stockDisponible = detallePedido.getProducto().getStock();
        if (detallePedidoDTO.getCantidad()>stockDisponible){
            throw new InvalidDataException();
        }

        detallePedido.setCantidad(detallePedidoDTO.getCantidad());
        detallePedido.setPrecioSubtotal(detallePedido.getCantidad()*detallePedido.getProducto().getPrecio());
    
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    @Transactional
    public void delete(Long id, Usuario usuarioLogueado) throws UnauthorizedException, NotFoundException {
        DetallePedido detallePedido = detallePedidoRepository.findById(id)
            .orElseThrow(NotFoundException::new);

        if (!detallePedido.getPedido().getCliente().getId().equals(usuarioLogueado.getId())) {
            throw new UnauthorizedException();
        }

        detallePedidoRepository.delete(detallePedido);
    }
}
